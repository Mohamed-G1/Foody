package com.example.foody.model

import com.google.gson.annotations.SerializedName

data class FoodJoke(
    @SerializedName("text")
    var textFoodJoke: String?
)
