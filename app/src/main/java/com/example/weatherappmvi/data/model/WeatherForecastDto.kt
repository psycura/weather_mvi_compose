package com.example.weatherappmvi.data.model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherForecastDto(
    val current: WeatherDto,
    val forecast: ForecastDto
)
