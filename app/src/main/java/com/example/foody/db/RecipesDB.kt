package com.example.foody.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.foody.db.entities.FavoriteEntity
import com.example.foody.db.entities.FoodJokeEntity
import com.example.foody.db.entities.RecipesEntity

@Database(
    entities = [RecipesEntity::class, FavoriteEntity::class, FoodJokeEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDB : RoomDatabase() {
    abstract fun recipesDao(): RecipesDao
}