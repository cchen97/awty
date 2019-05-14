package edu.washington.manjic.arewethereyet

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.app.AlarmManager
import android.app.AlarmManager.RTC_WAKEUP
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log


class  MainActivity : AppCompatActivity() {
    private var msg = ""
    private var num = ""
    private var min = 0
    private var minStr = ""
    private var alarmManager: AlarmManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val filter = IntentFilter()
        filter.addAction(BROADCAST)
        val receiver = BroadcastReceiver()
        registerReceiver(receiver, filter)

        val message = findViewById<EditText>(R.id.message)
        val number = findViewById<EditText>(R.id.number)
        val minutes = findViewById<EditText>(R.id.minutes)
        val btn = findViewById<Button>(R.id.button)
        btn.isEnabled = msg != "" && num != "" && min > 0

        alarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        message.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (s.toString() != msg) {
                    msg = s.toString()
                    btn.isEnabled = msg != "" && num != "" && min > 0
                }

            }
            override fun afterTextChanged(s: Editable) {}
        })

        number.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (s.toString() != num) {
                    num = s.toString()
                    btn.isEnabled = msg != "" && num != "" && min > 0
                }

            }
            override fun afterTextChanged(s: Editable) {}
        })

        minutes.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (s.toString() != minStr) {
                    minutes.removeTextChangedListener(this)
                    min = if (s.toString().toIntOrNull() == null) 0 else Integer.parseInt(s.toString())
                    minStr = min.toString()

                    minutes.setText(minStr)
                    minutes.setSelection(minStr.length)
                    minutes.addTextChangedListener(this)
                    btn.isEnabled = msg != "" && num != "" && min > 0
                }

            }

            override fun afterTextChanged(s: Editable) {}
        })

        btn.setOnClickListener {
            if (btn.text == "Start") {
                when {
                    msg == "" -> showErrorToast("Message to send can't be blank!")
                    num.length < 4 -> showErrorToast("Phone number must be standard length (between four and fifteen digits).")
                    num.length > 15 -> showErrorToast("Phone number must be standard length (between four and fifteen digits).")
                    min <= 0 -> showErrorToast("You must enter a time (in minutes) to pause between messages.")
                    min == 0 -> showErrorToast("The time between messages cannot be zero.")
                    else -> {
                        val intent = Intent(BROADCAST)
                            .putExtra(BroadcastReceiver.MESSAGE, msg)
                            .putExtra(BroadcastReceiver.TAR_PHONE_NUM, num)
                        val pendingIntent = PendingIntent.getBroadcast(
                            applicationContext,
                            0,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                        )
                        alarmManager!!.setRepeating(RTC_WAKEUP, System.currentTimeMillis(),
                            (min.toLong() * 60 * 1000),
                            pendingIntent
                        )
                        btn.text = "Stop"
                    }
                }
            } else {
                alarmManager!!.cancel(PendingIntent.getBroadcast(applicationContext, 0,
                    Intent(BROADCAST), PendingIntent.FLAG_UPDATE_CURRENT))
                Log.i(TAG, "Stopped")
                btn.text = "Start"
            }
        }

    }

    private fun showErrorToast(message: String) {
        Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
    }

    companion object{
        const val TAG = "MainActivity"
        const val BROADCAST = "edu.washington.manjic.arewethereyet.BROADCAST"
    }

}
