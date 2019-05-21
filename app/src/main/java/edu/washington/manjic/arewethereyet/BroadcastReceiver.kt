package edu.washington.manjic.arewethereyet

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.startActivity
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import android.os.Bundle



private const val TAG = "BroadcastReceiver"

class BroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        when (intent?.action) {
            MainActivity.BROADCAST -> {
                var phoneNum = intent.extras[TAR_PHONE_NUM] as String
                val msg = intent.extras[MESSAGE] as String
                if (phoneNum.length == 10) {
                    phoneNum = "(" + phoneNum.substring(0..2) + ") " + phoneNum.substring(3..5) + "-" + phoneNum.substring(6)
                }
                sendSMS(intent, context);

                Toast.makeText(context, "Texting $phoneNum : $msg", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendSMS(intent: Intent, context: Context) {
        var phoneNum = intent.extras[TAR_PHONE_NUM] as String
        val msg = intent.extras[MESSAGE] as String
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(
                phoneNum,
                null,
                msg,
                null,
                null)
    }

    companion object {
        const val MESSAGE = "edu.washington.manjic.arewethereyet.SMS_BODY"
        const val TAR_PHONE_NUM = "edu.washington.manjic.arewethereyet.TARGET_PHONE_NUMBER"
    }
}