package com.example.foody.data.network

import com.example.foody.db.RecipesDao
import com.example.foody.db.entities.FavoriteEntity
import com.example.foody.db.entities.FoodJokeEntity
import com.example.foody.db.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao
) {

    suspend fun insertRecipes(entity: RecipesEntity) {
        recipesDao.insertRecipes(entity)
    }

    fun readRecipes(): Flow<List<RecipesEntity>> {
        return recipesDao.readRecipes()
    }


    //those for favorite screen
    suspend fun insertFavoriteRecipe(entity: FavoriteEntity) {
        recipesDao.insertFavoriteRecipe(entity)
    }

    fun readFavoriteRecipe(): Flow<List<FavoriteEntity>> {
        return recipesDao.readFavoriteRecipe()
    }

    suspend fun deleteFavoriteRecipe(entity: FavoriteEntity) {
        recipesDao.deleteFavoriteRecipe(entity)
    }

    suspend fun deleteAllFavoriteRecipe() {
        recipesDao.deleteAllFavoriteRecipe()
    }

    // for food joke
    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity) {
        recipesDao.insertFoodJoke(foodJokeEntity)
    }

    fun readFoodJoke(): Flow<List<FoodJokeEntity>> {
        return recipesDao.readFoodJoke()
    }
}