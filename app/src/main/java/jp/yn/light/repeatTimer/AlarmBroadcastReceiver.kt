package jp.yn.light.repeatTimer

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        AlarmService.startService(context)
    }

    companion object {
        fun makePendingIntent(context: Context): PendingIntent {
            val intent = Intent(context, AlarmBroadcastReceiver::class.java)
            return PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        }
    }
}