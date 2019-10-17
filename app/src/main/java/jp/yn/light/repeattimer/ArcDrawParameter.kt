package jp.yn.light.repeattimer

data class ArcDrawParameter(
    val startAngle: Float,
    val sweepAngle: Float,
    val useCenter: Boolean
) {
    fun updateRate(rate: Float) = ArcDrawParameter(startAngle, rate * 360f, useCenter)

    companion object {
        fun createNewRateParameter(parameter: ArcDrawParameter?, rate: Float) =
            parameter?.updateRate(rate) ?: ArcDrawParameter(-90f, 0f, false)
    }
}