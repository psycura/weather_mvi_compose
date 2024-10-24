package com.example.weatherappmvi.data.repository

import com.example.weatherappmvi.data.mappers.toEntity
import com.example.weatherappmvi.data.network.ApiService
import com.example.weatherappmvi.domain.entity.Forecast
import com.example.weatherappmvi.domain.entity.Weather
import com.example.weatherappmvi.domain.repository.WeatherRepository
import org.koin.core.annotation.Single

@Single
class WeatherRepositoryImpl(
    private val apiService: ApiService
) : WeatherRepository {
    override suspend fun getWeather(cityId: Int): Weather {
        return apiService.loadCurrentWeather("${PREFIX_CITY_ID}$cityId").toEntity()
    }

    override suspend fun getForecast(cityId: Int): Forecast {
        return apiService.getForecast("${PREFIX_CITY_ID}$cityId").toEntity()
    }

    private companion object {
        private const val PREFIX_CITY_ID = "id:"
    }
}