package com.appgate.dsbsdk

import android.content.Context
import android.util.Log
import com.appgate.dsbsdk.util.DSBExceptionNumber.Companion.number
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import net.easysol.dsb.DSB
import net.easysol.dsb.application.exceptions.DSBException
import net.easysol.dsb.connection_protector.SecureCertificateResultHandler

class ConnectorApiPlugin(context: Context?) {
    private var sdk: DSB = DSB.sdk(context)


    fun isSecureByRiskRules(result: MethodChannel.Result) {
        val value: MutableList<Boolean> = ArrayList(1)
        value.add(sdk.CONNECTION_PROTECTOR_API.isSecureByRiskRules)
        result.success(value)
    }

    fun getRiskRulesStatus(result: MethodChannel.Result) {
        val value = HashMap<String, Any>()
        val data = sdk.CONNECTION_PROTECTOR_API.riskRulesStatus
        value[IS_SECURE_BY_RISK_RULES] = data.isSecureByRiskRules
        value[RISK_RULES_STATUS] = data.riskRulesStatus
        result.success(value)
    }

    fun isSecureCertificate(call: MethodCall, result: MethodChannel.Result) {
        val value: MutableList<Boolean> = ArrayList(1)
        val url = call.argument<String>(URL)
        sdk.CONNECTION_PROTECTOR_API.isSecureCertificate(url,
            object : SecureCertificateResultHandler {
                override fun onSuccess(isSecure: Boolean) {
                    value.add(isSecure)
                    result.success(value)
                }

                override fun onFailure(exception: DSBException) {
                    Log.e(TAG, "onFailure: ", exception)
                    result.error("${exception.number()}", exception.message, exception.localizedMessage)
                }
            })
    }


    companion object {
        private const val URL: String = "url"
        private const val IS_SECURE_BY_RISK_RULES: String = "isSecureByRiskRules"
        private const val RISK_RULES_STATUS: String = "riskRulesStatus"
        private var TAG = ConnectorApiPlugin::class.java.simpleName
    }
}