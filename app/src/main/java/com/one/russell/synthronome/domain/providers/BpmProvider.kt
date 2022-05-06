package com.one.russell.synthronome.domain.providers

import kotlinx.coroutines.flow.MutableStateFlow

class BpmProvider {

    val bpmFlow = MutableStateFlow(0)
}