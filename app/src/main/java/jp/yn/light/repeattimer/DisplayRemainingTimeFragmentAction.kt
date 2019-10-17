package jp.yn.light.repeattimer

class DisplayRemainingTimeFragmentAction private constructor(val type: Type, val remainingTime: RemainingTime = RemainingTime.createEmpty()) {
    enum class Type {
        START, STOP, SET_TIMER, LOADING
    }

    companion object {
        fun createTimer(remainingTime: RemainingTime) = DisplayRemainingTimeFragmentAction(Type.SET_TIMER, remainingTime)
        fun createStart() = DisplayRemainingTimeFragmentAction(Type.START)
        fun createStop() = DisplayRemainingTimeFragmentAction(Type.STOP)
        fun createLoading(remainingTime: RemainingTime) = DisplayRemainingTimeFragmentAction(Type.LOADING, remainingTime)
    }
}