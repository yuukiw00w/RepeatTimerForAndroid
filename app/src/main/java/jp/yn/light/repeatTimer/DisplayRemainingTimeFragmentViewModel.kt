package jp.yn.light.repeatTimer

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import jp.yn.light.extensions.observeOnChangedNonNull
import jp.yn.light.repeatTimer.DisplayRemainingTimeFragmentState.DisplayStatus
import jp.yn.light.arcView.ArcDrawParameter

class DisplayRemainingTimeFragmentViewModel(
        listener: DisplayRemainingTimeFragmentActionCreator.Listener,
        store: DisplayRemainingTimeFragmentStore,
        owner: () -> LifecycleOwner
) : ViewModel() {
    private val creator = DisplayRemainingTimeFragmentActionCreator(listener, store)
    val arcDrawParameter = MutableLiveData<ArcDrawParameter>().also {
        it.value = ArcDrawParameter.createCircleParameter()
    }
    val remainingTime = MutableLiveData<TimeStringResolver>().also {
        it.value = TimeStringResolver()
    }
    val isEnabledStopButton = MutableLiveData<Boolean>().also {
        it.value = false
    }
    val visibilityStartButton = MutableLiveData<Int>().also {
        it.value = View.VISIBLE
    }
    val visibilityPauseButton =
            Transformations.map(visibilityStartButton) { if (it == View.VISIBLE) View.GONE else View.VISIBLE }
    val isEnabledStartButton = MutableLiveData<Boolean>().also {
        it.value = false
    }
    val visibilityRemainingTime = MutableLiveData<Int>().also {
        it.value = View.INVISIBLE
    }
    val visibilityNumberPickers = MutableLiveData<Int>().also {
        it.value = View.INVISIBLE
    }
    val onClickStop = View.OnClickListener { creator.stopTimer() }
    val onClickPause = View.OnClickListener { creator.pauseTimer() }
    val onClickStart = View.OnClickListener { creator.startTimer() }
    val onClickNext = View.OnClickListener { creator.skipToNext() }
    val onClickPrevious = View.OnClickListener { creator.skipToPrevious() }

    fun changeTime(hour: Int, minute: Int, second: Int) {
        creator.changeTime(hour, minute, second)
    }

    fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            creator.handler.restore(savedInstanceState)
        } else {
            creator.handler.initialize()
        }
    }

    fun attach(context: Context) {
        creator.handler.attach(context)
    }

    fun onStart() {
        creator.handler.onStart()
    }

    fun onStop() {
        creator.handler.onStop()
    }

    fun save(outState: Bundle) = creator.handler.save(outState)

    init {
        store.state.observeOnChangedNonNull(owner()) { state ->
            val newValue = ArcDrawParameter.createNewRateParameter(
                    arcDrawParameter.value,
                    state.alarmTime.rate
            )
            arcDrawParameter.value = newValue
            when (state.displayStatus) {
                DisplayStatus.INITIALIZING -> Unit
                DisplayStatus.PAUSE -> {
                    isEnabledStopButton.value = true
                    isEnabledStartButton.value = true
                    visibilityStartButton.value = View.VISIBLE
                    visibilityRemainingTime.value = View.VISIBLE
                    visibilityNumberPickers.value = View.GONE

                    val remainingTimeValue = state.alarmTime.remainingTime
                    remainingTime.value = TimeStringResolver(remainingTimeValue)
                }
                DisplayStatus.STOP -> {
                    isEnabledStopButton.value = true
                    isEnabledStartButton.value = true
                    visibilityStartButton.value = View.VISIBLE
                    visibilityRemainingTime.value = View.INVISIBLE
                    visibilityNumberPickers.value = View.VISIBLE

                    val remainingTimeValue = state.alarmTime.remainingTime
                    remainingTime.value = TimeStringResolver(remainingTimeValue)
                }
                DisplayStatus.COUNT_DOWN -> {
                    isEnabledStopButton.value = true
                    isEnabledStartButton.value = true
                    visibilityStartButton.value = View.GONE
                    visibilityRemainingTime.value = View.VISIBLE
                    visibilityNumberPickers.value = View.GONE

                    val remainingTimeValue = state.alarmTime.remainingTime
                    remainingTime.value = TimeStringResolver(remainingTimeValue)
                }
            }
        }
    }
}