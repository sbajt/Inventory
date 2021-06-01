package com.superology.inventory.notifications

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.superology.inventory.databases.FirebaseDataService

class FirebaseNotificationService : FirebaseMessagingService() {

    private val TAG = FirebaseNotificationService::class.java.canonicalName

    override fun onNewToken(token: String) {
        Log.d(TAG, "New firebase token generated")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        if (!message.data.isNullOrEmpty()) {
            when (message.data.getValue("notificationType")) {
                "important" -> {
                    if (!NotificationUtils.hasUserSentImportant) {
                        NotificationUtils.createImportant(this)
                    }
                }
                "statusChanged" -> {
                    if (NotificationUtils.hasUserSentImportant) {
                        NotificationUtils.hasUserSentImportant = false
                        NotificationUtils.createStatusChanged(this)
                    }
                }
            }
        }
    }

    override fun onMessageSent(msg: String) {
        Log.d(TAG, "Message $msg sent")
    }

    override fun onSendError(msgId: String, ex: Exception) {
        Log.e(TAG, "Sending message error", ex)
    }
}