package jp.yn.light.repeatTimer

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import jp.yn.light.preferences.InternalPreferenceAccessor
import jp.yn.light.preferences.PreferenceKey

class BootReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        if (intent?.action != Intent.ACTION_BOOT_COMPLETED) {
            return
        }
        val key = PreferenceKey.Internal.LongKey.NEXT_ALARM_TIME
        val nextAlarmTime = InternalPreferenceAccessor(context).getLong(key)
        if (nextAlarmTime <= 0L) {
            return
        }
        if (nextAlarmTime <= System.currentTimeMillis()) {
            AlarmService.startService(context)
            return
        }
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        check(alarmManager is AlarmManager)
        val pendingIntent = AlarmBroadcastReceiver.makePendingIntent(context)
        alarmManager.setAlarmClock(AlarmManager.AlarmClockInfo(nextAlarmTime, null), pendingIntent)
    }
}