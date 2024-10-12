package com.example.weatherappmvi.data.model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherCurrentDto(
    val current: WeatherDto,
)
