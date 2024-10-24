package com.example.weatherappmvi.data.mappers

import com.example.weatherappmvi.data.model.WeatherCurrentDto
import com.example.weatherappmvi.data.model.WeatherDto
import com.example.weatherappmvi.data.model.WeatherForecastDto
import com.example.weatherappmvi.domain.entity.Forecast
import com.example.weatherappmvi.domain.entity.Weather
import java.util.Calendar
import java.util.Date

fun WeatherCurrentDto.toEntity(): Weather = current.toEntity()

fun WeatherDto.toEntity(): Weather = Weather(
    temperature = tempC,
    conditionText = condition.text,
    iconUrl = condition.iconUrl.correctImageUrl(),
    date = date.toCalendar()
)

fun WeatherForecastDto.toEntity(): Forecast = Forecast(
    currentWeather = current.toEntity(),
    upcoming = forecast.forecastDay.drop(1).map { dayDto ->
        val dayWeatherDto = dayDto.day

        Weather(
            temperature = dayWeatherDto.tempC,
            conditionText = dayWeatherDto.condition.text,
            iconUrl = dayWeatherDto.condition.iconUrl.correctImageUrl(),
            date = dayDto.date.toCalendar()
        )
    }
)

private fun Long.toCalendar(): Calendar = Calendar.getInstance().apply {
    time = Date(this@toCalendar * 1000)
}

private fun String.correctImageUrl(): String = "https:$this".replace("64x64", "128x128")