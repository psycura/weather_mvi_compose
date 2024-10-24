package com.example.weatherappmvi.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_cities")
data class CityDbModel(
    @PrimaryKey val id: Int,
    val name: String,
    val country: String,
)
