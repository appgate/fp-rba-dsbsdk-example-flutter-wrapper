package com.appgate.dsbsdk

import android.content.Context
import io.flutter.plugin.common.MethodChannel
import net.easysol.dsb.DSB

/** DeviceProtectorPlugin */
class DeviceProtectorPlugin(context: Context?) {
    private var sdk: DSB = DSB.sdk(context)

    fun isDeviceRooted(result: MethodChannel.Result) {
        val value: MutableList<Boolean> = ArrayList(1)
        value.add(sdk.DEVICE_PROTECTOR_API.isDeviceRooted)
        result.success(value)
    }

    fun isDeviceOnInsecureNetwork(result: MethodChannel.Result) {
        val value: MutableList<Boolean> = ArrayList(1)
        value.add(sdk.DEVICE_PROTECTOR_API.isDeviceOnInsecureNetwork)
        result.success(value)
    }

    fun isDeviceHostsFileInfected(result: MethodChannel.Result) {
        val value: MutableList<Boolean> = ArrayList(1)
        value.add(sdk.DEVICE_PROTECTOR_API.isDeviceHostsFileInfected)
        result.success(value)
    }

    fun getDeviceHostsFileInfections(result: MethodChannel.Result) {
        result.success(sdk.DEVICE_PROTECTOR_API.deviceHostsFileInfections)
    }

    fun restoreDeviceHosts(result: MethodChannel.Result) {
        val value: MutableList<String> = ArrayList(1)
        value.add("")
        sdk.DEVICE_PROTECTOR_API.restoreDeviceHosts()
        result.success(value)
    }
}