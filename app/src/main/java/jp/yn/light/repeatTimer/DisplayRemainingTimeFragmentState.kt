package jp.yn.light.repeatTimer

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DisplayRemainingTimeFragmentState(
    val displayStatus: DisplayStatus,
    val alarmTime: AlarmTime
) : Parcelable {
    enum class DisplayStatus {
        INITIALIZING, PAUSE, STOP, COUNT_DOWN;

        companion object {
            fun from(value: String?) = if (value == null) INITIALIZING else valueOf(value)
        }
    }

    constructor(parcel: Parcel?) : this(
        DisplayStatus.from(parcel?.readString()),
        parcel?.readParcelable<AlarmTime>(AlarmTime::class.java.classLoader)
            ?: AlarmTime.createEmpty()
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(displayStatus.name)
        dest?.writeParcelable(alarmTime, 0)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<DisplayRemainingTimeFragmentState> {
        override fun createFromParcel(source: Parcel?) = DisplayRemainingTimeFragmentState(source)

        override fun newArray(size: Int): Array<DisplayRemainingTimeFragmentState?> =
            arrayOfNulls(size)
    }
}