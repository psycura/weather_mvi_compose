package com.example.weatherappmvi.data.repository

import com.example.weatherappmvi.data.local.db.FavoriteCitiesDao
import com.example.weatherappmvi.data.mappers.toDbModel
import com.example.weatherappmvi.data.mappers.toEntities
import com.example.weatherappmvi.domain.entity.City
import com.example.weatherappmvi.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class FavoriteRepositoryImpl(
    private val favoriteCitiesDao: FavoriteCitiesDao
) : FavoriteRepository {
    override val favoriteCities: Flow<List<City>> = favoriteCitiesDao.getFavoriteCities()
        .map { it.toEntities() }

    override fun observeIsFavorite(cityId: Int): Flow<Boolean> =
        favoriteCitiesDao.observeIsFavorite(cityId)

    override suspend fun addToFavorite(city: City) {
        favoriteCitiesDao.addToFavorite(city.toDbModel())
    }

    override suspend fun removeFromFavorite(cityId: Int) {
        favoriteCitiesDao.removeFromFavorite(cityId)
    }
}