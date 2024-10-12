package com.example.weatherappmvi.domain.entity

import java.util.Calendar

data class Weather(
    val temperature: Float,
    val conditionText: String,
    val iconUrl: String,
    val date: Calendar
)
