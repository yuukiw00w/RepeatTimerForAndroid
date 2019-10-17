package jp.yn.light.repeattimer

data class RemainingTime(val remainingTime: Long, val specifiedTime: Long, val rate: Float) {
    companion object {
        fun createEmpty() = RemainingTime(0L, 0L, 0f)
        fun createFromRate(rate: Float) = RemainingTime(0L, 0L, rate)
    }
}