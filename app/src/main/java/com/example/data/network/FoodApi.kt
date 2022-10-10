package com.example.data.network

import com.example.foody.model.FoodRecipeModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface FoodApi {

    @GET("/recipes/complexSearch")
    suspend fun getRecipes(
        //queryMap we used it if there are more than queries
        @QueryMap queries: Map<String, String>
    ): Response<FoodRecipeModel>
}