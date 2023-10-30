package com.appgate.dsbsdk

import android.app.Application
import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.appgate.dsbsdk.constants.DSBMethodChannelNames.*
import com.appgate.dsbsdk.constants.DSBModulesNames.*
import com.appgate.dsbsdk.util.DSBExceptionNumber.Companion.number
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.PluginRegistry
import net.easysol.dsb.DSB
import net.easysol.dsb.application.exceptions.DSBException
import net.easysol.dsb.licensing.InitializationResultHandler


/** DsbsdkPlugin */
class DsbsdkPlugin : FlutterPlugin, MethodCallHandler, ActivityAware, PluginRegistry.RequestPermissionsResultListener {

    private var context: Context? = null
    private var application: Application? = null
    private var sdk: DSB? = null

    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var dsbSdkChannel: MethodChannel
    private lateinit var deviceProtectorChannel: MethodChannel
    private lateinit var connectionProtectorChannel: MethodChannel
    private lateinit var malwareProtectorChannel: MethodChannel
    private lateinit var smsProtectorChannel: MethodChannel

    private lateinit var deviceProtector: DeviceProtectorPlugin
    private lateinit var connectionProtector: ConnectorApiPlugin
    private lateinit var malwareProtector: MalwareProtectorPlugin
    private lateinit var smsProtector: SMSProtectorApiPlugin

    private fun initDSB() {
        this.sdk = DSB.sdk(context)
    }

    /* ActivityAware */

    override fun onAttachedToActivity(activity: ActivityPluginBinding) {
        this.context = activity.activity
        this.application = activity.activity.application
        this.initDSB()
    }

    override fun onReattachedToActivityForConfigChanges(activity: ActivityPluginBinding) {
        this.context = activity.activity
        this.application = activity.activity.application
        this.initDSB()
        // Set permissions listener
        activity.addRequestPermissionsResultListener(this)
        // Init APIs
        deviceProtector = DeviceProtectorPlugin(context)
        connectionProtector = ConnectorApiPlugin(context)
        malwareProtector = MalwareProtectorPlugin(application)
        smsProtector = SMSProtectorApiPlugin(application, activity)
    }

    override fun onDetachedFromActivity() {}
    override fun onDetachedFromActivityForConfigChanges() {}

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        dsbSdkChannel = MethodChannel(flutterPluginBinding.binaryMessenger, DSB_SDK.value)
        dsbSdkChannel.setMethodCallHandler(this)
        deviceProtectorChannel =
            MethodChannel(flutterPluginBinding.binaryMessenger, DEVICE_PROTECTOR_MODULE.value)
        deviceProtectorChannel.setMethodCallHandler(this)
        connectionProtectorChannel =
            MethodChannel(flutterPluginBinding.binaryMessenger, CONNECTION_PROTECTOR_MODULE.value)
        connectionProtectorChannel.setMethodCallHandler(this)
        malwareProtectorChannel =
            MethodChannel(flutterPluginBinding.binaryMessenger, MALWARE_PROTECTOR_MODULE.value)
        malwareProtectorChannel.setMethodCallHandler(this)
        smsProtectorChannel =
            MethodChannel(flutterPluginBinding.binaryMessenger, SMS_PROTECTOR_MODULE.value)
        smsProtectorChannel.setMethodCallHandler(this)
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            INIT_WITH_LICENSE.value ->
                initWithLicense(call, result)
            GET_DEVICE_ID.value ->
                getDeviceID(result)
            SEND_LOGIN_DATA.value ->
                sendLoginData(call)
            IS_DEVICE_ROOTED.value ->
                deviceProtector.isDeviceRooted(result)
            IS_DEVICE_ON_INSECURE_NETWORK.value ->
                deviceProtector.isDeviceOnInsecureNetwork(result)
            IS_DEVICE_HOSTS_FILE_INFECTED.value ->
                deviceProtector.isDeviceHostsFileInfected(result)
            GET_DEVICE_HOSTS_FILE_INFECTIONS.value ->
                deviceProtector.getDeviceHostsFileInfections(result)
            RESTORE_DEVICE_HOSTS.value ->
                deviceProtector.restoreDeviceHosts(result)
            IS_SECURE_BY_RISK_RULES.value ->
                connectionProtector.isSecureCertificate(result)
            GET_RISK_RULES_STATUS.value ->
                connectionProtector.getRiskRulesStatus(result)
            IS_SECURE_CERTIFICATE.value ->
                connectionProtector.isSecureCertificate(call, result)
            START_OVERLAPPING_PROTECTION.value ->
                malwareProtector.startOverlappingProtection(result)
            CONFIGURE_OVERLAPPING_MALWARE_GUI_NOTIFICATION.value ->
                malwareProtector.configureOverlappingMalwareGUINotification(call, result)
            SET_OVERLAY_LISTENER.value -> malwareProtector.setOverlayListener(
                malwareProtectorChannel
            )
            SET_OVERLAY_GUI_NOTIFICATION_ENABLE.value ->
                malwareProtector.setOverlayGUINotificationEnable(call, result)
            SET_OVERLAY_TOAST_NOTIFICATION_ENABLE.value ->
                malwareProtector.setOverlayToastNotificationEnable(call, result)
            CONFIGURE_OVERLAPPING_MALWARE_TOAST_NOTIFICATION.value ->
                malwareProtector.configureOverlappingMalwareToastNotification(call, result)
            REQUEST_SMS_PERMISSIONS.value ->
                smsProtector.requestPermissions(call, result)
            START_MESSAGE_MONITORING.value ->
                smsProtector.startMessageMonitoring(call, result)
        }
    }

    private fun initWithLicense(call: MethodCall, result: MethodChannel.Result) {
        val listener = object : InitializationResultHandler {
            override fun onSuccess() {
                Log.d(TAG, "onSuccess()")
                val value: MutableList<String> = ArrayList(1)
                value.add("")
                result.success(value)
            }

            override fun onFailure(exception: DSBException) {
                Log.e(TAG, "onFailure: ", exception)
                result.error("${exception.number()}", exception.message, exception.localizedMessage)
            }
        }
        val domain = call.argument<String>(DOMAIN);
        if (!TextUtils.isEmpty(domain)) {
            this.sdk?.init(call.argument(LICENCE), domain, listener)
        } else {
            this.sdk?.init(call.argument(LICENCE), listener)
        }
    }

    private fun getDeviceID(result: MethodChannel.Result) {
        val value: MutableList<String> = ArrayList(1)
        value.add(this.sdk?.deviceID ?: "")
        result.success(value)
    }

    private fun sendLoginData(call: MethodCall) {
        try {
            val data = call.argument<Map<String, String>?>(DATA);
            if (data != null) {
                this.sdk?.sendLoginData(data)
            }
        } catch (e: Exception) {
            Log.e(TAG, "sendLoginData: ", e)
        }
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        dsbSdkChannel.setMethodCallHandler(null)
        deviceProtectorChannel.setMethodCallHandler(null)
        connectionProtectorChannel.setMethodCallHandler(null)
        malwareProtectorChannel.setMethodCallHandler(null)
    }

    companion object {
        private var TAG = DsbsdkPlugin::class.java.simpleName
        private const val DOMAIN: String = "domain"
        private const val LICENCE: String = "licence"
        private const val DATA: String = "data"
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray): Boolean {
        if (requestCode == SMSProtectorApiPlugin.REQUEST_CODE_DSB_PERMISSION) {
            smsProtector.onRequestPermissionsResult(permissions, grantResults)
            return true
        }
        return false
    }
}
