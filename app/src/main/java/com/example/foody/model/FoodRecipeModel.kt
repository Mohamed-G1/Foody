package com.example.foody.model


import com.google.gson.annotations.SerializedName

data class FoodRecipeModel(
    @SerializedName("results")
    val results: List<RecipesResult>
)