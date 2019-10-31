package jp.yn.light.arcView

import android.graphics.Paint
import androidx.annotation.ColorInt

data class ArcInitData(@ColorInt val color: Int,
                       val strokeWidth: Float,
                       val isAntiAlias: Boolean,
                       val style: Paint.Style,
                       val strokeCap: Paint.Cap)