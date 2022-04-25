package com.one.russell.metroman_20.presentation.views.beat_types

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View

class OnPressTouchListener(
    private val onTouchDown: () -> Unit,
    private val onTouchUp: () -> Unit
) : View.OnTouchListener {
    var isPressed = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        isPressed = when (event.action) {
            MotionEvent.ACTION_DOWN -> true
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> false
            else -> return false
        }

        if (isPressed) onTouchDown()
        else onTouchUp()

        return true
    }
}