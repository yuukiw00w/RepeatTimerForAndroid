package jp.yn.light.repeattimer

import android.content.Context
import java.util.concurrent.TimeUnit

class TimeStringResolver(private val time: Long) {
    companion object {
        private val minute = TimeUnit.MINUTES.toMillis(1L)
        private val hour = TimeUnit.HOURS.toMillis(1L)
        private val day = TimeUnit.DAYS.toMillis(1L)
    }

    fun parse(context: Context): String {
        return when (time) {
            in 0 until minute -> context.getString(
                R.string.second_milli,
                TimeUnit.MILLISECONDS.toSeconds(time) % 60L,
                time % 1000L
            )
            in minute until hour -> context.getString(
                R.string.minute_second,
                TimeUnit.MILLISECONDS.toMinutes(time) % 60L,
                TimeUnit.MILLISECONDS.toSeconds(time) % 60L
            )
            in hour until day -> context.getString(
                R.string.hour_minute_second,
                TimeUnit.MILLISECONDS.toHours(time) % 24L,
                TimeUnit.MILLISECONDS.toMinutes(time) % 60L,
                TimeUnit.MILLISECONDS.toSeconds(time) % 60L
            )
            in day..Long.MAX_VALUE -> context.getString(
                R.string.day_hour,
                TimeUnit.MILLISECONDS.toDays(time),
                TimeUnit.MILLISECONDS.toHours(time) % 24L
            )
            else -> context.getString(
                R.string.second_milli,
                TimeUnit.MILLISECONDS.toSeconds(time) % 60L,
                time % 1000L
            )
        }
    }
}