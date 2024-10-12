package com.example.weatherappmvi.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConditionDto(
    @SerialName("text")
    val text: String,

    @SerialName("icon")
    val iconUrl: String

)
