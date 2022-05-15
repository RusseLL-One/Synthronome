package com.one.russell.synthronome.domain

import kotlinx.serialization.Serializable

@Serializable
data class Bookmark(
    val id: String = "",
    val bpm: Int = 0,
    val beatTypesList: List<BeatType> = emptyList(),
    val isSelected: Boolean = false
)