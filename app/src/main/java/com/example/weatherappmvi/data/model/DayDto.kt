package com.example.weatherappmvi.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DayDto(
    @SerialName("date_epoch")
    val date: Long,

    val day: DayWeatherDto
)
