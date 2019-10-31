package jp.yn.light.repeatTimer

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator

class RemainingTimeAnimator(
    specifiedTime: Long,
    remainingTimeValue: Long,
    onUpdate: (AlarmTime) -> Unit,
    onEnd: (AlarmTime) -> Unit
) {
    private val animator: ValueAnimator =
        ValueAnimator.ofFloat(0f, 1f).also { animator ->
            animator.duration = remainingTimeValue
            animator.addUpdateListener { animation ->
                val remainingRate =
                    (1f - animation.animatedFraction) * remainingTimeValue.toFloat() / specifiedTime.toFloat()
                val remainingTime =
                    (specifiedTime.toFloat() * remainingRate).toLong()
                onUpdate(AlarmTime(remainingTime, specifiedTime, remainingRate))
            }
            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    onEnd(AlarmTime(0L, specifiedTime, 0f))
                }

                override fun onAnimationCancel(animation: Animator?) {
                    animation?.removeAllListeners()
                }
            })
            animator.interpolator = LinearInterpolator()
        }

    fun start() {
        animator.start()
    }

    fun stop() {
        animator.cancel()
    }
}