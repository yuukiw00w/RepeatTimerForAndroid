package jp.yn.light.repeattimer

import android.view.View
import androidx.lifecycle.*
import java.util.concurrent.TimeUnit

class DisplayRemainingTimeFragmentViewModel(
    vibratorAccessor: VibratorAccessor,
    store: DisplayRemainingTimeFragmentStore,
    owner: () -> LifecycleOwner
) : ViewModel() {
    private val creator = DisplayRemainingTimeFragmentActionCreator(store)
    val arcDrawParameter = MutableLiveData<ArcDrawParameter>()
    val remainingTime = MutableLiveData<TimeStringResolver>()
    val isEnabledRestartButton = MutableLiveData<Boolean>()
    val isEnabledStopButton = MutableLiveData<Boolean>()
    val isStopped = MutableLiveData<Boolean>()
    val onClickRestart = View.OnClickListener {
        creator.onRestart()
    }
    val onClickStartStop = View.OnClickListener {
        creator.onStartStop()
    }
    val onClickChangeRepeat = View.OnClickListener {
        // 繰り返しの間隔を変更する画面に遷移する
    }

    init {
        store.state.observeOnChangedNonNull(owner()) { state ->
            isEnabledRestartButton.postValue(!state.isLoading)
            isEnabledStopButton.postValue(!state.isLoading)
            isStopped.postValue(state.isStopped)
            if (state.isStopped) {
                return@observeOnChangedNonNull
            }

            val newValue = ArcDrawParameter.createNewRateParameter(
                arcDrawParameter.value,
                state.remainingTime.rate
            )
            arcDrawParameter.postValue(newValue)
            val remainingTimeValue = state.remainingTime.remainingTime
            if (remainingTimeValue >= 0L) {
                remainingTime.postValue(TimeStringResolver(remainingTimeValue))
            }
            if (remainingTimeValue <= 0L) {
                creator.onStop()
                vibratorAccessor.vibrate(50000)
            }
        }
        creator.onInit()
    }

    /**
     * 残件
     * ・時間に到達した時のバイブレーション
     * ・停止→スタートでの経過時間維持
     * ・設定時間
     */
}