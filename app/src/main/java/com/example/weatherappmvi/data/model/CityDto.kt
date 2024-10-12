package com.example.weatherappmvi.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CityDto(
    val id: Int,
    val name: String,
    val country: String,
)
