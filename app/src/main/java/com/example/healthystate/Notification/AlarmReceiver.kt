package com.example.healthystate.Notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.healthystate.AutoAndReg.MainActivity
import com.example.healthystate.R
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(getNotificationText(hour))
            .setContentText(getNotificationMessage(hour))
            .setSmallIcon(R.drawable.ic_email)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channelName = "Notifications"
        val channelDescription = "All notifications"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, channelName, importance)
        channel.description = channelDescription
        notificationManager.createNotificationChannel(channel)
    }

    private fun getNotificationText(hour: Int): String {
        return when (hour) {
            in 6..10 -> "Желаем вам хорошего дня!"
            11 -> "Удачной тренировки!"
            12 -> "Не забудьте выпить сегодня достаточное количество воды!"
            in 13..17 -> "Отлично справляетесь!"
            in 19..20 -> "Распишите свой сегодняшний день!"
            in 18..23 -> "Спокойной ночи"
            else -> "Доброе утро"
        }
    }

    private fun getNotificationMessage(hour: Int): String {
        return when (hour) {
            in 6..10 -> "Доброе утро! Надеемся, вы начали свой день с положительной мыслью."
            11 -> "Не забудьте взять воду на тренировку!"
            12 -> "Сейчас время обновить свой организм водой."
            in 13..17 -> "Не забудьте отмечать свои достижения!"
            in 19..20 -> "Мы уверены, что вы потрудились сегодня на славу!"
            in 18..23 -> "Спокойной ночи! Желаем сладких снов и отличного отдыха."
            else -> "Сегодня обещает быть замечательным днем!"
        }
    }

    companion object {
        const val CHANNEL_ID = "notification_channel_id"
        const val NOTIFICATION_ID = 1
    }
}
