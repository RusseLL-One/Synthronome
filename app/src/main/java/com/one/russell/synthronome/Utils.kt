package com.one.russell.synthronome

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.*
import kotlinx.coroutines.launch

fun LifecycleOwner.repeatOnResume(f: suspend () -> Unit) {
    lifecycleScope.launch {
        lifecycle.whenResumed {
            f.invoke()
        }
    }
}

fun Number.toPx() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics
)

val Number.sp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )

fun Int.bpmToMs(): Float = 1000f * 60f / this

fun Context.getStyledAttributes(
    attributeSet: AttributeSet?,
    styleArray: IntArray,
    defStyleAttr: Int,
    block: TypedArray.() -> Unit
) = this.obtainStyledAttributes(attributeSet, styleArray, defStyleAttr, 0).let {
    try {
        it.block()
    } finally {
        it.recycle()
    }
}

@ColorInt
fun Context.getColorCompat(@ColorRes id: Int): Int {
    return ResourcesCompat.getColor(resources, id, theme)
}