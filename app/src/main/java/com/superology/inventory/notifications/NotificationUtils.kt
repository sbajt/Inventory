package com.superology.inventory.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.superology.inventory.R
import com.superology.inventory.activities.MainActivity


object NotificationUtils {

    private val TAG = NotificationUtils::class.java.canonicalName
    private const val ID_IMPORTANT = 9001
    private const val ID_STATUS_FREE = 9002

    var hasUserSentImportant = false
    private lateinit var notificationManager: NotificationManager

    fun init() {
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
    }

    fun createChannel(context: Context) {
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel =
            NotificationChannel(
                context.getString(R.string.notification_channel_id),
                context.getString(R.string.notification_channel_id),
                NotificationManager.IMPORTANCE_DEFAULT
            )

        if (notificationManager.getNotificationChannel(context.getString(R.string.notification_channel_id)) == null)
            notificationManager.createNotificationChannel(channel)
    }

    fun removeChannel(context: Context) {
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (notificationManager.getNotificationChannel(context.getString(R.string.notification_channel_id)) != null) {
            notificationManager.deleteNotificationChannel(context.getString(R.string.notification_channel_id))
        }
    }

    fun createTopic(context: Context) {
        FirebaseMessaging.getInstance()
            .subscribeToTopic(context.getString(R.string.notification_topic))
            .addOnFailureListener {
                Log.e(TAG, context.getString(R.string.notification_topic_error), it)
            }

    }

    fun createServiceNotification(context: Context): Notification {
        return context.let {
            NotificationCompat.Builder(it, context.getString(R.string.notification_channel_id))
                .setContentTitle(context.getString(R.string.notification_service_title))
                .setContentText(context.getString(R.string.notification_service_text))
                .setSmallIcon(R.drawable.ic_launcher)
                .build()
        }
    }

    fun createImportant(context: Context) {
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (notificationManager.activeNotifications?.find { it.id == ID_IMPORTANT } == null) {
            val notification = NotificationCompat.Builder(
                context,
                context.getString(R.string.notification_channel_id)
            )
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle(context.getString(R.string.notification_important_title))
                .setContentText(context.getString(R.string.notification_important_text))
                .setSmallIcon(R.drawable.ic_important)
                .setContentIntent(
                    PendingIntent.getActivity(
                        context,
                        0,
                        Intent(context, MainActivity::class.java).apply {
                            flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        },
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                )
                .build()
            notificationManager.notify(ID_IMPORTANT, notification)
        }
    }

    fun createStatusChanged(context: Context) {
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (notificationManager.activeNotifications?.find { it.id == ID_STATUS_FREE } == null) {
            val notification = NotificationCompat.Builder(
                context,
                context.getString(R.string.notification_channel_id)
            )
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle(context.getString(R.string.notification_status_title))
                .setContentText(context.getString(R.string.notification_status_text))
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(
                    PendingIntent.getActivity(
                        context,
                        0,
                        Intent(context, MainActivity::class.java).apply {
                            flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        },
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                )
                .build()
            notificationManager.notify(ID_STATUS_FREE, notification)
        }
    }

    fun removeImportant(context: Context) {
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(ID_IMPORTANT)
    }
}
