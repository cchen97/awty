package edu.washington.manjic.arewethereyet

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

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

                Toast.makeText(context, "Texting $phoneNum : $msg", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val MESSAGE = "edu.washington.manjic.arewethereyet.SMS_BODY"
        const val TAR_PHONE_NUM = "edu.washington.manjic.arewethereyet.TARGET_PHONE_NUMBER"
    }
}