package jp.yn.light.preferences

import android.content.Context

class InternalPreferenceAccessor(context: Context) {
    private val preference = context.getSharedPreferences("internal", Context.MODE_PRIVATE)
    private val editor = preference.edit()

    fun putLong(longKey: PreferenceKey.Internal.LongKey, value: Long) {
        editor.putLong(longKey.key(), value)
    }

    fun putString(stringKey: PreferenceKey.Internal.StringKey, value: String) {
        editor.putString(stringKey.key(), value)
    }

    fun getLong(longKey: PreferenceKey.Internal.LongKey) =
        preference.getLong(longKey.key(), longKey.defaultValue)

    fun getString(stringKey: PreferenceKey.Internal.StringKey) =
        preference.getString(stringKey.key(), stringKey.defaultValue)

    fun removeLong(longKey: PreferenceKey.Internal.LongKey) {
        editor.remove(longKey.key())
    }

    fun removeString(stringKey: PreferenceKey.Internal.StringKey) {
        editor.remove(stringKey.key())
    }

    fun apply() = editor.apply()

    fun commit() = editor.commit()
}