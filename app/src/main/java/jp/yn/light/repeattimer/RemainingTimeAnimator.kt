package jp.yn.light.repeattimer

import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator

class RemainingTimeAnimator(duration: Long, onUpdate: (RemainingTime) -> Unit) {
    private val animator: ValueAnimator =
        ValueAnimator.ofFloat(0f, 1f).also { animator ->
            animator.duration = duration
            animator.addUpdateListener { animation ->
                val remainingRate = (1f - animation.animatedFraction)
                val remainingTime = (duration.toFloat() * (1f - animation.animatedFraction)).toLong()
                onUpdate(RemainingTime(remainingTime, duration, remainingRate))
            }
            animator.interpolator = LinearInterpolator()
        }

    fun start() {
        animator.start()
    }

    fun stop() {
        animator.cancel()
    }
}