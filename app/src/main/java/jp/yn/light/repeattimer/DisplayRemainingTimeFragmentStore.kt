package jp.yn.light.repeattimer

import androidx.lifecycle.MutableLiveData

class DisplayRemainingTimeFragmentStore {
    val state = MutableLiveData<DisplayRemainingTimeFragmentState>()

    fun dispatch(action: DisplayRemainingTimeFragmentAction) {
        state.value = reduce(state.value, action)
    }

    private fun reduce(
        state: DisplayRemainingTimeFragmentState?,
        action: DisplayRemainingTimeFragmentAction
    ): DisplayRemainingTimeFragmentState {
        return when (action.type) {
            DisplayRemainingTimeFragmentAction.Type.START -> DisplayRemainingTimeFragmentState(
                isLoading = false,
                isStopped = false,
                remainingTime = action.remainingTime
            )
            DisplayRemainingTimeFragmentAction.Type.STOP -> DisplayRemainingTimeFragmentState(
                isLoading = false,
                isStopped = true,
                remainingTime = action.remainingTime
            )
            DisplayRemainingTimeFragmentAction.Type.SET_TIMER -> DisplayRemainingTimeFragmentState(
                state?.isLoading ?: false,
                state?.isStopped ?: true,
                action.remainingTime
            )
            DisplayRemainingTimeFragmentAction.Type.LOADING -> DisplayRemainingTimeFragmentState(
                true,
                state?.isStopped ?: true,
                action.remainingTime
            )
        }
    }
}