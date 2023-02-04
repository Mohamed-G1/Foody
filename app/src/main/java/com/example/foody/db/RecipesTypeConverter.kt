package com.example.foody.db

import androidx.room.TypeConverter
import com.example.foody.model.FoodRecipeModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipesTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun foodRecipesToString(foodRecipeModel: FoodRecipeModel): String {
        return gson.toJson(foodRecipeModel)
    }

    @TypeConverter
    fun foodRecipesFromString(data: String): FoodRecipeModel {
        val listType = object : TypeToken<FoodRecipeModel>() {}.type
        return gson.fromJson(data, listType)
    }
}