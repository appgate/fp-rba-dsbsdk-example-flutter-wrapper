package com.appgate.dsbsdk.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import net.easysol.dsb.DSB
import net.easysol.dsb.application.exceptions.ParseMessageIntentError
import net.easysol.dsb.sms_protector.entities.MessageReceived


open abstract class ProtectorReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        try {
            onMessageReceived(context, DSB.sdk(context).SMS_PROTECTOR_API.parseMessageIntent(intent))
        } catch (parseMessageIntent: ParseMessageIntentError) {
            parseMessageIntent.printStackTrace()
        }
    }

    abstract fun onMessageReceived(context: Context, message: MessageReceived)
}
