package com.one.russell.metroman_20.data

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

inline fun <reified T> T.serialize(): String = Json.encodeToString(this)
inline fun <reified T> String.deserialize(): T = Json.decodeFromString(this)
