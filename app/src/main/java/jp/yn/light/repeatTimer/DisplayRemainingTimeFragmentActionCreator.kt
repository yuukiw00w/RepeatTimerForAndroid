package jp.yn.light.repeatTimer

import android.content.Context
import android.os.Bundle
import com.squareup.moshi.Moshi
import jp.yn.light.preferences.InternalPreferenceAccessor
import jp.yn.light.preferences.PreferenceKey.Internal
import jp.yn.light.repeatTimer.DisplayRemainingTimeFragmentState.DisplayStatus
import java.util.concurrent.TimeUnit

class DisplayRemainingTimeFragmentActionCreator(
    private val listener: Listener,
    private val store: DisplayRemainingTimeFragmentStore
) {
    interface Listener {
        fun generateAlarm()
        fun cancelAlarm()
        fun setAlarmClock(nextAlarmTime: Long)
        fun resetAlarmClock()
    }

    private var remainingTimeAnimator: RemainingTimeAnimator? = null
    private var accessor: InternalPreferenceAccessor? = null
    val handler = LifeCycleHandler()
    private val adapter =
        Moshi.Builder().build().adapter(DisplayRemainingTimeFragmentState::class.java)

    inner class LifeCycleHandler {
        fun onStop() {
            val state = store.state.value
            check(state != null)
            val alarmTime = state.alarmTime
            if (state.displayStatus == DisplayStatus.COUNT_DOWN) {
                val alarmClockTime = System.currentTimeMillis() + alarmTime.remainingTime
                listener.setAlarmClock(alarmClockTime)
                accessor?.putLong(Internal.LongKey.NEXT_ALARM_TIME, alarmClockTime)
            }
            val json = adapter.toJson(state)
            accessor?.putString(Internal.StringKey.DISPLAY_REMAINING_TIME_STATE, json)
            accessor?.apply()
        }

        fun onStart() {
            listener.resetAlarmClock()
            val key = Internal.StringKey.DISPLAY_REMAINING_TIME_STATE
            val json = accessor?.getString(key)
            accessor?.removeString(key)
            accessor?.apply()
            val newState = if (json != null) adapter.fromJson(json) else null
            if (newState == null) {
                stopTimer()
                return
            }
            if (newState.displayStatus == DisplayStatus.COUNT_DOWN) {
                val nextTime = accessor?.getLong(Internal.LongKey.NEXT_ALARM_TIME)
                accessor?.removeLong(Internal.LongKey.NEXT_ALARM_TIME)
                accessor?.apply()
                check(nextTime != null)
                val remainingTime = nextTime - System.currentTimeMillis()
                if (remainingTime <= 0L) {
                    val alarmTime = AlarmTime(0L, newState.alarmTime.specifiedTime, 0f)
                    pauseTimer(alarmTime)
                } else {
                    val alarmTime = AlarmTime(remainingTime, newState.alarmTime.specifiedTime)
                    store.dispatch(DisplayRemainingTimeFragmentAction.createTimer(alarmTime))
                    startTimer()
                }
                return
            }
            store.restore(newState)
        }

        fun initialize() {
            listener.cancelAlarm()
            val action = DisplayRemainingTimeFragmentAction.createInitialize()
            store.dispatch(action)
        }

        fun attach(context: Context) {
            accessor = InternalPreferenceAccessor(context)
        }

        fun restore(savedInstanceState: Bundle) {
            val isRestored = store.restore(savedInstanceState)
            if (!isRestored) {
                initialize()
            }
        }

        fun save(outState: Bundle) {
            store.save(outState)
        }
    }

    fun stopTimer() {
        listener.cancelAlarm()
        remainingTimeAnimator?.stop()
        val action = DisplayRemainingTimeFragmentAction.createStop(store.state.value?.alarmTime)
        store.dispatch(action)
    }

    fun startTimer() {
        listener.cancelAlarm()
        val alarmTime = store.state.value?.alarmTime
        check(alarmTime != null)

        val time = alarmTime.remainingTime
        val specifiedTime = alarmTime.specifiedTime
        val accessor = accessor
        check(accessor != null)
        accessor.commit()

        val remainingTime = if (time == 0L) specifiedTime else time

        remainingTimeAnimator =
            RemainingTimeAnimator(specifiedTime, remainingTime, onUpdate = { newAlarmTime ->
                store.dispatch(DisplayRemainingTimeFragmentAction.createTimer(newAlarmTime))
            }, onEnd = { newAlarmTime ->
                listener.generateAlarm()
                pauseTimer(newAlarmTime)
            })
        store.dispatch(
            DisplayRemainingTimeFragmentAction.createStart(
                AlarmTime(
                    time,
                    specifiedTime,
                    1f
                )
            )
        )
        remainingTimeAnimator?.start()
    }

    fun skipToNext() {
        listener.cancelAlarm()
        remainingTimeAnimator?.stop()
        val action = DisplayRemainingTimeFragmentAction.createStop(store.state.value?.alarmTime)
        store.dispatch(action)
    }

    fun skipToPrevious() {
        listener.cancelAlarm()
        remainingTimeAnimator?.stop()
        val action = DisplayRemainingTimeFragmentAction.createStop(store.state.value?.alarmTime)
        store.dispatch(action)
    }

    fun changeTime(hour: Int, minute: Int, second: Int) {
        val hourMill = TimeUnit.HOURS.toMillis(hour.toLong())
        val minuteMill = TimeUnit.MINUTES.toMillis(minute.toLong())
        val secondMill = TimeUnit.SECONDS.toMillis(second.toLong())
        val value = hourMill + minuteMill + secondMill
        store.dispatch(DisplayRemainingTimeFragmentAction.createStop(AlarmTime.createStopTime(value)))
    }

    fun pauseTimer(alarmTime: AlarmTime? = store.state.value?.alarmTime) {
        remainingTimeAnimator?.stop()
        val action = DisplayRemainingTimeFragmentAction.createPause(alarmTime)
        store.dispatch(action)
    }
}