package com.example.weatherappmvi.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherDto(
    @SerialName("last_updated_epoch")
    val date: Long,

    @SerialName("temp_c")
    val tempC: Float,

    val condition: ConditionDto

)
