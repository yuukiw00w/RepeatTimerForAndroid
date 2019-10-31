package jp.yn.light.repeatTimer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import jp.yn.light.notification.NotificationChannelId

class AlarmService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        VibratorAccessor(this).vibrate(5000L)
        startForeground(SERVICE_ID, createNotification())
        return START_NOT_STICKY
    }

    private fun createNotification(): Notification {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        val channelId = NotificationChannelId.ALARM
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val id = channelId.id()
            val channelName = getString(channelId.channelNameId)
            val channel = NotificationChannel(id, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager?.createNotificationChannel(channel)
        }
        return NotificationCompat.Builder(this, channelId.id())
            .setPriority(
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) NotificationCompat.PRIORITY_HIGH
                else NotificationManager.IMPORTANCE_DEFAULT
            )
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setContentTitle(getString(R.string.notification_alarm_title))
            .setContentText(getString(R.string.notification_alarm_text))
            .setSmallIcon(R.drawable.ic_access_alarm_black_24dp)
            .setContentIntent(
                RepeatTimerTopActivity.makePendingIntent(
                    this,
                    RepeatTimerTopActivity.From.ALARM_NOTIFICATION
                )
            )
            .setOngoing(true)
            .build()
    }

    companion object {
        private const val SERVICE_ID = 1

        fun startService(context: Context) {
            val intent = Intent(context, AlarmService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stopService(context: Context) {
            val intent = Intent(context, AlarmService::class.java)
            context.stopService(intent)
        }
    }
}