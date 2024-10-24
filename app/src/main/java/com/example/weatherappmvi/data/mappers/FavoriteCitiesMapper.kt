package com.example.weatherappmvi.data.mappers

import com.example.weatherappmvi.data.local.model.CityDbModel
import com.example.weatherappmvi.domain.entity.City

fun City.toDbModel(): CityDbModel = CityDbModel(id = id, name = name, country = country)

fun CityDbModel.toEntity(): City = City(id = id, name = name, country = country)

fun List<CityDbModel>.toEntities(): List<City> = map { it.toEntity() }