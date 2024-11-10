package com.example.healthystate.AutoAndReg

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.healthystate.Notification.AlarmReceiver
import com.example.healthystate.Notification.MyNotificationService
import com.example.healthystate.PaperDbClass
import com.example.healthystate.R
import io.paperdb.Paper
import java.util.*


class MainActivity : AppCompatActivity() {

    private val CHANNEL_ID = "default_channel_id"


    var dialogReg: Dialog? = null
    var dialogLog: Dialog? = null
    private lateinit var paperDbClass: PaperDbClass
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_main)
        startService(Intent(this, MyNotificationService::class.java))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }

        Paper.init(applicationContext)
        paperDbClass = PaperDbClass()


        if (paperDbClass != null) {
            val user = paperDbClass.getUser()
            if (user != null) {
                showLoginDialog()
            } else {
            }
        } else {
        }

        val channelName = "default_channel"
        val channelDesc = "Default channel for app notifications"
        val importance = NotificationManager.IMPORTANCE_HIGH

        val channel = NotificationChannel(CHANNEL_ID, channelName, importance).apply {
            description = channelDesc
        }

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        createNotificationChannel()

        val intent1 = Intent(this, AlarmReceiver::class.java).apply {
            putExtra("message", "Хорошего дня!")
        }
        val pendingIntent1 = PendingIntent.getBroadcast(
            this,
            0,
            intent1,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val intent2 = Intent(this, AlarmReceiver::class.java).apply {
            putExtra("message", "Не забудьте выпить воды!")
        }


        val pendingIntent2 = PendingIntent.getBroadcast(
            this,
            1,
            intent2,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val intent3 = Intent(this, AlarmReceiver::class.java).apply {
            putExtra("message", "Не забудьте выпить воды!")
        }

        val pendingIntent3 = PendingIntent.getBroadcast(
            this,
            2,
            intent2,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val calendar1 = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        val calendar2 = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 19)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        val calendar3 = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 22)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar1.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent1
        )


        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar2.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent2
        )

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar3.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent3
        )



        dialogReg = Dialog(this@MainActivity)
        dialogLog = Dialog(this@MainActivity)


        val loginButton = findViewById<Button>(R.id.loginBut)
        loginButton.setOnClickListener{
            showLoginDialog();
        }

        val regButton = findViewById<Button>(R.id.regBut)
        regButton.setOnClickListener{
            showRegDialog();
        }

    }

    private fun showRegDialog() {
        RegDialog().show(supportFragmentManager, "RegDialog")

    }

    private fun showLoginDialog() {
        LoginDialog().show(supportFragmentManager, "LoginDialog")
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Default Channel"
            val descriptionText = "This is the default channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }}

}