package pers.vaccae.servicedemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


class MessageReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if(MESSAGE_ACTION == intent.action){
            val messageid = intent.getStringExtra(MESSAGE_ID)
            messageid?.let {
                Log.d(TAG, it)
                val notification = NotificationUtil.mNotifiCationBuilder
                    .setContentTitle("前台服务测试")
                    .setContentText(it)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(androidx.loader.R.drawable.notification_bg)
                    .setContentIntent(NotificationUtil.mPendingIntent)
                    .setSound(null)
                    .build()
                NotificationUtil.mNotificationManager.notify(1, notification)
            }
        }
    }


}