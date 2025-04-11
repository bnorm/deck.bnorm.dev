package dev.bnorm.kc25.broadcast

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class BroadcastMessage(
    val sceneIndex: Int,
    val stateIndex: Int,
)
