package com.one.russell.metroman_20.domain.providers

import kotlinx.coroutines.flow.MutableStateFlow

class BpmProvider {

    val bpmFlow = MutableStateFlow(0)
}