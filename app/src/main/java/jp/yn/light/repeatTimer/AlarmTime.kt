package jp.yn.light.repeatTimer

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AlarmTime(
    val remainingTime: Long,
    val specifiedTime: Long,
    val rate: Float = remainingTime.toFloat() / specifiedTime.toFloat()
) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readLong(), parcel.readLong(), parcel.readFloat())

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest ?: return
        dest.writeLong(remainingTime)
        dest.writeLong(specifiedTime)
        dest.writeFloat(rate)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<AlarmTime> {
        fun createEmpty() = AlarmTime(0L, 0L, 1f)
        fun createStopTime(specifiedTime: Long) = AlarmTime(specifiedTime, specifiedTime, 1f)

        override fun createFromParcel(parcel: Parcel) = AlarmTime(parcel)
        override fun newArray(size: Int): Array<AlarmTime?> = arrayOfNulls(size)
    }
}