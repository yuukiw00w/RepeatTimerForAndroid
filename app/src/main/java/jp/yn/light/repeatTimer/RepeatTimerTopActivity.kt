package jp.yn.light.repeatTimer

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class RepeatTimerTopActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repeat_timer_top)
    }

    enum class From(val requestCode: Int) {
        ALARM_NOTIFICATION(1)
    }

    companion object {
        fun makePendingIntent(context: Context, from: From): PendingIntent {
            val intent = Intent(context, RepeatTimerTopActivity::class.java)
            return PendingIntent.getActivity(context, from.requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        }
    }
}
