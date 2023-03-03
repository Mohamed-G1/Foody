package com.example.foody.data.network

import com.example.foody.model.FoodJoke
import com.example.foody.model.FoodRecipeModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface FoodApi {

    @GET("/recipes/complexSearch")
    suspend fun getRecipes(
        //queryMap we used it if there are more than queries
        @QueryMap queries: Map<String, String>
    ): Response<FoodRecipeModel>

    @GET("/recipes/complexSearch")
    suspend fun searchRecipes(
        //queryMap we used it if there are more than queries
        @QueryMap searchQueries: Map<String, String>
    ): Response<FoodRecipeModel>

    @GET("food/jokes/random")
    suspend fun getFoodJoke(
        @Query("apiKey") apiKey: String
    ): Response<FoodJoke>
}