package com.example.weatherappmvi.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherappmvi.data.local.model.CityDbModel

@Database(entities = [CityDbModel::class], version = 1, exportSchema = false)
abstract class FavoriteDatabase : RoomDatabase() {

    abstract fun favoriteCitiesDao(): FavoriteCitiesDao

    companion object {

        private const val DB_NAME = "FavoriteDatabase"

        private var INSTANCE: FavoriteDatabase? = null
        private val LOCK = Any()

        fun getInstance(context: Context): FavoriteDatabase {
            INSTANCE?.let { return it }

            synchronized(LOCK) {
                INSTANCE?.let { return it }

                val db = Room.databaseBuilder(
                    context,
                    FavoriteDatabase::class.java,
                    DB_NAME
                ).build()

                INSTANCE = db

                return db
            }

        }
    }
}