package jp.yn.light.arcView

data class ArcDrawParameter(
    val startAngle: Float,
    val sweepAngle: Float,
    val useCenter: Boolean
) {
    fun updateRate(rate: Float) = ArcDrawParameter(startAngle, rate * 360f, useCenter)

    companion object {
        fun createNewRateParameter(parameter: ArcDrawParameter?, rate: Float) =
            parameter?.updateRate(rate) ?: createCircleParameter()

        fun createCircleParameter() = ArcDrawParameter(-90f, 360f, false)
    }
}