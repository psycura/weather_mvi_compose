package com.example.weatherappmvi.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastDto(
    @SerialName("forecastday") val forecastDay: List<DayDto>
)
