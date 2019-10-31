package jp.yn.light.notification

import androidx.annotation.StringRes
import java.util.*

/**
 * new NotificationCompat.Builder(context)がOreoからdeprecatedになって、
 * コンストラクタにChannelIdの指定が必要になった
 */
enum class NotificationChannelId(@StringRes val channelNameId: Int) {
    ALARM(R.string.notification_channel_alarm_name);

    fun id() = this.name.toLowerCase(Locale.JAPAN)
}