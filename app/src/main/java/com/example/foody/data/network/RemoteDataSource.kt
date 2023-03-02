package com.example.foody.data.network

import com.example.foody.model.FoodJoke
import com.example.foody.model.FoodRecipeModel
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodApi: FoodApi
) {
    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipeModel> {
        return foodApi.getRecipes(queries)
    }

    suspend fun searchRecipes(searchQueries: Map<String, String>): Response<FoodRecipeModel> {
        return foodApi.searchRecipes(searchQueries)
    }

    suspend fun getFoodJoke(apiKey: String): Response<FoodJoke> {
        return foodApi.getFoodJoke(apiKey)
    }
}