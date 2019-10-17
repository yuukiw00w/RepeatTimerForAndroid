package jp.yn.light.repeattimer

import android.content.Context
import android.os.VibrationEffect
import android.os.VibrationEffect.DEFAULT_AMPLITUDE
import android.os.Vibrator

class VibratorAccessor(context: Context?) {
    private val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    fun vibrate(milliseconds: Long) {
        vibrate(longArrayOf(0, milliseconds))
    }

    fun vibrate(pattern: LongArray, repeat: Int = -1) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val vibrationEffect = VibrationEffect.createWaveform(pattern, intArrayOf(0, DEFAULT_AMPLITUDE), repeat)
            vibrator?.vibrate(vibrationEffect)
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(pattern, repeat)
        }
    }
}