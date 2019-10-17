package jp.yn.light.repeattimer

import android.animation.ValueAnimator
import android.view.animation.Animation
import android.view.animation.LinearInterpolator

class ArcRepeatAnimator(duration: Long, onUpdate: (Float) -> Unit) {
    private val animator =
        ValueAnimator.ofFloat(0f, 1f).also { animator ->
            animator.duration = duration
            animator.addUpdateListener { animation ->
                onUpdate(animation.animatedFraction)
            }
            animator.repeatCount = Animation.INFINITE
            animator.interpolator = LinearInterpolator()
        }

    fun start() {
        animator.start()
    }

    fun stop() {
        animator.cancel()
    }
}