package com.example.data.network

import com.example.foody.model.FoodRecipeModel
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodApi: FoodApi
) {
    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipeModel> {
        return foodApi.getRecipes(queries)
    }
}