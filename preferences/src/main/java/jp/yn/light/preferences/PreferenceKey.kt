package jp.yn.light.preferences

import java.util.*

class PreferenceKey {
    class Internal {
        enum class LongKey(val defaultValue: Long) {
            NEXT_ALARM_TIME(0L);

            fun key() = name.toLowerCase(Locale.JAPAN)
        }
        enum class StringKey(val defaultValue: String?) {
            DISPLAY_REMAINING_TIME_STATE(null);

            fun key() = name.toLowerCase(Locale.JAPAN)
        }
    }
}