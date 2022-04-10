package com.one.russell.metroman_20

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.TypedValue
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.one.russell.metroman_20.domain.Constants.MAX_BPM
import com.one.russell.metroman_20.domain.Constants.MIN_BPM
import kotlinx.coroutines.launch

fun Fragment.repeatOnResume(f: suspend () -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            f.invoke()
        }
    }
}

fun Number.toPx() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics
)

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