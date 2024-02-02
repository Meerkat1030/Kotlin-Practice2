package com.example.class20240202

import android.app.Notification
import android.app.NotificationManager
import android.app.RemoteInput
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class ReplyReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val replyTxt = RemoteInput.getResultsFromIntent(intent)
            ?.getCharSequence("key_text_reply")
        Log.d("test", "replyTxt : ${replyTxt}")

        val manager = context?.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager

        manager.cancel(11)
    }
}