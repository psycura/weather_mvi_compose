package com.example.weatherappmvi.domain.repository

import com.example.weatherappmvi.domain.entity.Forecast
import com.example.weatherappmvi.domain.entity.Weather

interface WeatherRepository {
    suspend fun getWeather(cityId: Int): Weather
    suspend fun getForecast(cityId: Int): Forecast
}