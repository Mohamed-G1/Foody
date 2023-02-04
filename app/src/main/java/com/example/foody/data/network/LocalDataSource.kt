package com.example.foody.data.network

import com.example.foody.db.RecipesDao
import com.example.foody.db.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao
) {

    suspend fun insertRecipes(entity: RecipesEntity) {
        recipesDao.insertRecipes(entity)
    }

    fun readDatabase(): Flow<List<RecipesEntity>> {
        return recipesDao.readRecipes()
    }
}