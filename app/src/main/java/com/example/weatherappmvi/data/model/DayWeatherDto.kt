package com.example.weatherappmvi.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DayWeatherDto(
    @SerialName("avgtemp_c")
    val tempC: Float,

    val condition: ConditionDto
)
