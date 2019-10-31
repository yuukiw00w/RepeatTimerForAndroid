package jp.yn.light.repeatTimer

import android.os.Bundle
import android.util.Log
import androidx.annotation.UiThread
import androidx.lifecycle.MutableLiveData

class DisplayRemainingTimeFragmentStore {
    val state = MutableLiveData<DisplayRemainingTimeFragmentState>()

    @UiThread
    fun dispatch(action: DisplayRemainingTimeFragmentAction) {
        Log.e("test", action.type.name)
        state.value = reduce(state.value, action)
    }

    @UiThread
    fun restore(savedInstanceState: Bundle): Boolean {
        val savedState =
            savedInstanceState.getParcelable<DisplayRemainingTimeFragmentState>(SAVE_KEY)
                ?: return false
        restore(savedState)
        return true
    }

    @UiThread
    fun restore(newState: DisplayRemainingTimeFragmentState?) {
        state.value = newState
    }

    fun save(outState: Bundle) {
        outState.putParcelable(SAVE_KEY, state.value)
    }

    private fun reduce(
        state: DisplayRemainingTimeFragmentState?,
        action: DisplayRemainingTimeFragmentAction
    ): DisplayRemainingTimeFragmentState {
        if (state == null) {
            return DisplayRemainingTimeFragmentState(
                DisplayRemainingTimeFragmentState.DisplayStatus.INITIALIZING,
                alarmTime = action.alarmTime
            )
        }
        return when (action.type) {
            DisplayRemainingTimeFragmentAction.Type.START -> DisplayRemainingTimeFragmentState(
                DisplayRemainingTimeFragmentState.DisplayStatus.COUNT_DOWN,
                alarmTime = action.alarmTime
            )
            DisplayRemainingTimeFragmentAction.Type.PAUSE -> DisplayRemainingTimeFragmentState(
                DisplayRemainingTimeFragmentState.DisplayStatus.PAUSE,
                alarmTime = action.alarmTime
            )
            DisplayRemainingTimeFragmentAction.Type.STOP -> DisplayRemainingTimeFragmentState(
                DisplayRemainingTimeFragmentState.DisplayStatus.STOP,
                alarmTime = AlarmTime(action.alarmTime.specifiedTime, action.alarmTime.specifiedTime, 1f)
            )
            DisplayRemainingTimeFragmentAction.Type.SET_TIMER -> DisplayRemainingTimeFragmentState(
                state.displayStatus,
                action.alarmTime
            )
            DisplayRemainingTimeFragmentAction.Type.SET_INITIALIZE -> DisplayRemainingTimeFragmentState(
                DisplayRemainingTimeFragmentState.DisplayStatus.INITIALIZING,
                action.alarmTime
            )
        }
    }

    companion object {
        private const val SAVE_KEY = "display_remaining_time_fragment_state_key"
    }
}