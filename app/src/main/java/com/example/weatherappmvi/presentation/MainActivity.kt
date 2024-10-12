package com.example.weatherappmvi.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.weatherappmvi.data.network.ApiService
import com.example.weatherappmvi.presentation.ui.theme.WeatherAppMVITheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.KoinAndroidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val apiService = ApiService()

        CoroutineScope(Dispatchers.Main).launch {
            val result = apiService.loadCurrentWeather(query = "London")
            val forecast = apiService.getForecast(query = "London")
            val cities = apiService.search(query = "London")
            Log.d("MainActivity", "Current weather: $result\n Forecast: $forecast\n Cities: $cities")
        }

        setContent {
            KoinAndroidContext() {
                WeatherAppMVITheme {

                }
            }

        }
    }
}
