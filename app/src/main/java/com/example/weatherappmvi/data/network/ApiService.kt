package com.example.weatherappmvi.data.network

import com.example.weatherappmvi.BuildConfig
import com.example.weatherappmvi.data.model.CityDto
import com.example.weatherappmvi.data.model.WeatherCurrentDto
import com.example.weatherappmvi.data.model.WeatherForecastDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.annotation.Single


@Single
class ApiService() {

    private companion object {
        const val BASE_URL = "https://api.weatherapi.com/v1/"
    }

    private val client = HttpClient(OkHttp) {
        expectSuccess = true
        engine {
            config {
                followRedirects(true)
            }
            addInterceptor { chain ->
                val originalRequest = chain.request()
                val newUrl = originalRequest.url.newBuilder()
                    .addQueryParameter("key", BuildConfig.WEATHER_API_KEY)
                    .build()

                val newRequest = originalRequest.newBuilder()
                    .url(newUrl)
                    .build()

                chain.proceed(newRequest)
            }
            addInterceptor(HttpLoggingInterceptor().apply {
                setLevel(
                    HttpLoggingInterceptor.Level.BODY
                )
            })
        }
        install(Logging) {
            level = LogLevel.BODY
        }
        defaultRequest {
            url(BASE_URL)
        }
        install(ContentNegotiation) {
            json(
                json = Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                    isLenient = true
                }
            )
        }
    }

    suspend fun loadCurrentWeather(
        query: String,
    ): WeatherCurrentDto =
        client.get("current.json") {
            url {
                parameters.append("q", query)
            }
        }.body()

    suspend fun getForecast(
        query: String,
        days: Int = 4,
    ): WeatherForecastDto =
        client.get("forecast.json") {
            url {
                parameters.append("q", query)
                parameters.append("days", days.toString())
            }
        }.body()

    suspend fun search(
        query: String,
    ): List<CityDto> =
        client.get("search.json") {
            url {
                parameters.append("q", query)
            }
        }.body()
}