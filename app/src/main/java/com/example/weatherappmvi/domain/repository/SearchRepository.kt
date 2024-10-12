package com.example.weatherappmvi.domain.repository

import com.example.weatherappmvi.domain.entity.City

interface SearchRepository {
    suspend fun search(query: String): List<City>
}