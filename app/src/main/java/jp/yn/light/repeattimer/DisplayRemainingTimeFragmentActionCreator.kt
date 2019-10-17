package jp.yn.light.repeattimer

import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class DisplayRemainingTimeFragmentActionCreator(private val store: DisplayRemainingTimeFragmentStore) {

    private var remainingTimeAnimator: RemainingTimeAnimator? = null
    private var arcRepeatAnimator: ArcRepeatAnimator? = null

    fun onRestart() {
        onStart()
    }

    fun onStartStop() {
        arcRepeatAnimator?.stop()
        if (store.state.value?.isStopped == true) {
            onStart()
        } else {
            remainingTimeAnimator?.stop()
            store.dispatch(DisplayRemainingTimeFragmentAction.createStop())
        }
    }

    fun onStop() {
        if (store.state.value?.isStopped != true) {
            remainingTimeAnimator?.stop()
            store.dispatch(DisplayRemainingTimeFragmentAction.createStop())
        }
    }

    fun onStart(specifiedTime: Long = TimeUnit.SECONDS.toMillis(300L)) {
        remainingTimeAnimator = RemainingTimeAnimator(specifiedTime) { remainingTime ->
            store.dispatch(DisplayRemainingTimeFragmentAction.createTimer(remainingTime))
        }
        store.dispatch(DisplayRemainingTimeFragmentAction.createStart())
        remainingTimeAnimator?.start()
    }

    fun onInit() {
        arcRepeatAnimator = ArcRepeatAnimator(10L) { rate ->
            val remainingTime = RemainingTime.createFromRate(rate)
            val action = DisplayRemainingTimeFragmentAction.createLoading(remainingTime)
            store.dispatch(action)
        }
        arcRepeatAnimator?.start()
        // ここでデータをロードして、完了したらonStart
        GlobalScope.launch(Dispatchers.Main) {
            delay(50)
            onStartStop()
            onStart()
        }
    }
}