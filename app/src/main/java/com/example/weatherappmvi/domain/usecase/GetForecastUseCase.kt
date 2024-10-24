package com.example.weatherappmvi.domain.usecase

import com.example.weatherappmvi.domain.repository.WeatherRepository
import org.koin.core.annotation.Factory

@Factory
class GetForecastUseCase(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(cityId: Int) = weatherRepository.getForecast(cityId)
}