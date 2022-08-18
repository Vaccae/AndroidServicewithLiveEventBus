package pers.vaccae.servicedemo

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

/**
 * 作者：Vaccae
 * 邮箱：3657447@qq.com
 * 创建时间： 12:17
 * 功能模块说明：
 */
class NotificationUtil {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var mNotifiCationBuilder: NotificationCompat.Builder

        lateinit var mNotificationManager: NotificationManager

        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context

        lateinit var mPendingIntent: PendingIntent

        fun <T> getInstance(context: Context, clazz: Class<T>, packageName: String) {

            mContext = context
            mNotificationManager =
                context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val intent = Intent(mContext, clazz)
            mPendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    packageName,
                    packageName,
                    NotificationManager.IMPORTANCE_HIGH
                )
                channel.enableLights(true)
                channel.setShowBadge(true)
                channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                mNotificationManager?.let { mng ->
                    mng.createNotificationChannel(channel)
                    mNotifiCationBuilder =
                        NotificationCompat.Builder(mContext, "default")
                            .setChannelId(packageName)
                }
            } else {
                mNotifiCationBuilder =
                    NotificationCompat.Builder(mContext, "default")
            }
        }
    }
}