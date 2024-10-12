package com.example.weatherappmvi.domain.usecase

import com.example.weatherappmvi.domain.repository.SearchRepository
import org.koin.core.annotation.Factory

class SearchUseCase(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(query: String) = searchRepository.search(query)
}