package com.one.russell.metroman_20.domain.providers

import android.graphics.Color
import com.one.russell.metroman_20.domain.Colors
import kotlinx.coroutines.flow.MutableStateFlow

class ColorsProvider {

    val colorFlow = MutableStateFlow(Colors(Color.BLACK, Color.BLACK,Color.BLACK))
}