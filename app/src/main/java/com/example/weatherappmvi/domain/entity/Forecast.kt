package com.example.weatherappmvi.domain.entity

data class Forecast(
    val currentWeather: Weather,
    val upcoming: List<Weather>,
)
