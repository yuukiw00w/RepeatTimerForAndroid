package jp.yn.light.repeatTimer

class DisplayRemainingTimeFragmentAction private constructor(val type: Type, val alarmTime: AlarmTime) {
    enum class Type {
        START, STOP, PAUSE, SET_TIMER, SET_INITIALIZE
    }

    companion object {
        fun createTimer(alarmTime: AlarmTime) = DisplayRemainingTimeFragmentAction(Type.SET_TIMER, alarmTime)
        fun createStart(alarmTime: AlarmTime) = DisplayRemainingTimeFragmentAction(Type.START, alarmTime)
        fun createStop(alarmTime: AlarmTime?) = DisplayRemainingTimeFragmentAction(Type.STOP, AlarmTime.createStopTime(alarmTime?.specifiedTime ?: 0L))
        fun createPause(alarmTime: AlarmTime?) = DisplayRemainingTimeFragmentAction(Type.PAUSE, alarmTime ?: AlarmTime.createEmpty())
        fun createInitialize() = DisplayRemainingTimeFragmentAction(Type.SET_INITIALIZE, AlarmTime.createEmpty())
    }
}