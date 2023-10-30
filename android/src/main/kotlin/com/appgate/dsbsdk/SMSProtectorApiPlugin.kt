package com.appgate.dsbsdk

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import com.appgate.dsbsdk.util.DSBExceptionNumber.Companion.number
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import net.easysol.dsb.DSB
import net.easysol.dsb.application.exceptions.DSBException
import net.easysol.dsb.sms_protector.MessageMonitoringResultHandler
import net.easysol.logging.DSBLogMessages.*


class SMSProtectorApiPlugin(private val application: Application?, private val activity: ActivityPluginBinding) {

    private var sdk: DSB = DSB.sdk(application)
    private var context: Context = application!!.applicationContext

    private lateinit var permissionsNeeded: ArrayList<String>
    private lateinit var permissionsCallback: MethodChannel.Result

    fun requestPermissions(call: MethodCall, result: MethodChannel.Result) {
        permissionsNeeded = ArrayList<String>()
        if (context.checkSelfPermission(Manifest.permission.READ_SMS) !== PackageManager.PERMISSION_GRANTED)
            permissionsNeeded.add(Manifest.permission.READ_SMS)
        if (context.checkSelfPermission(Manifest.permission.RECEIVE_SMS) !== PackageManager.PERMISSION_GRANTED)
            permissionsNeeded.add(Manifest.permission.RECEIVE_SMS)
        if (context.checkSelfPermission(Manifest.permission.RECEIVE_MMS) !== PackageManager.PERMISSION_GRANTED)
            permissionsNeeded.add(Manifest.permission.RECEIVE_MMS)

        if(permissionsNeeded.isEmpty()) {
            val value: MutableList<Boolean> = ArrayList(1)
            value.add(true)
            result.success(value)
        } else if(!validatePermissionManifests()) {
            result.error("113", MISSING_MANIFETS_PERMISSION, null)
        } else {
            activity.getActivity().requestPermissions(permissionsNeeded.toArray(arrayOfNulls<String>(permissionsNeeded.size)), REQUEST_CODE_DSB_PERMISSION)
            permissionsCallback = result
        }
    }

    fun onRequestPermissionsResult(permissions: Array<out String>, grantResults: IntArray) {
        if(permissionsNeeded == null || permissionsCallback == null) return
        for (i in permissions.indices) {
            val permission = permissions[i]
            val grantResult = grantResults[i]
            if (grantResult == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.remove(permission)
            }
        }
        if(permissionsNeeded.isEmpty()) {
            val value: MutableList<Boolean> = ArrayList(1)
            value.add(true)
            permissionsCallback.success(value)
        } else {
            permissionsCallback.error("115", SMS_READ_PERMISSION_DENY, permissionsNeeded)
        }
    }

    fun startMessageMonitoring(call: MethodCall, result: MethodChannel.Result) {
        val packageName = call.argument<String?>(PACKAGE_NAME)
        val className = call.argument<String?>(CLASS_NAME)
        val value: MutableList<Boolean> = ArrayList(1)
        val handler = object : MessageMonitoringResultHandler {
            override fun onSuccess() {
                value.add(true)
                result.success(value)
            }
            override fun onFailure(exception: DSBException?) {
                if(exception != null) {
                    result.error("${exception!!.number()}", exception!!.message, exception!!.localizedMessage)
                } else {
                    result.error("", "", "")
                }
            }
        }
        if(packageName != null && className != null) {
            sdk.SMS_PROTECTOR_API.startMessageMonitoring(packageName!!, className!!, handler)
        } else {
            sdk.SMS_PROTECTOR_API.startMessageMonitoring(handler)
        }
    }

    private fun validatePermissionManifests(): Boolean {
        var permissions = ArrayList<String>()
        try {
            val packageInfo = context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.GET_PERMISSIONS
            )
            if (packageInfo != null && packageInfo.requestedPermissions.isNotEmpty()) {
                for (permission in packageInfo.requestedPermissions) {
                    if(permissionsNeeded.contains(permission)) {
                        permissions.add(permission);
                    }
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {}
        return permissions.size == permissionsNeeded.size;
    }

    companion object {
        const val REQUEST_CODE_DSB_PERMISSION = 8
        private const val PACKAGE_NAME: String = "package"
        private const val CLASS_NAME: String = "class"
    }

}