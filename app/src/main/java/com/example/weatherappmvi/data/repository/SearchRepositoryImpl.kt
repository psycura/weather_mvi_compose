package com.example.weatherappmvi.data.repository

import com.example.weatherappmvi.data.mappers.toEntities
import com.example.weatherappmvi.data.network.ApiService
import com.example.weatherappmvi.domain.entity.City
import com.example.weatherappmvi.domain.repository.SearchRepository
import org.koin.core.annotation.Single

@Single
class SearchRepositoryImpl(
    private val apiService: ApiService
) : SearchRepository {
    override suspend fun search(query: String): List<City> {
        return apiService.search(query).toEntities()
    }
}