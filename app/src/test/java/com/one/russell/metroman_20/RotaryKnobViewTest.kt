package com.one.russell.metroman_20

import com.one.russell.metroman_20.presentation.views.rotary_knob.RotaryKnobView
import org.junit.Test
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

internal class RotaryKnobViewTest {

    @Test
    fun isClockwise() {
        assertTrue(RotaryKnobView.isClockwise(30f, 40f))
        assertFalse(RotaryKnobView.isClockwise(40f, 30f))
        assertTrue(RotaryKnobView.isClockwise(350f, 10f))
        assertFalse(RotaryKnobView.isClockwise(10f, 350f))
    }
}