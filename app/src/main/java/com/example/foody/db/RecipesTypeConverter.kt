package com.example.foody.db

import androidx.room.TypeConverter
import com.example.foody.model.FoodRecipeModel
import com.example.foody.model.RecipesResult
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

    @TypeConverter
    fun resultToString(result: RecipesResult): String {
        return gson.toJson(result)
    }

    @TypeConverter
    fun resultFromString(result: String): RecipesResult {
        val listType = object : TypeToken<RecipesResult>() {}.type
        return gson.fromJson(result, listType)
    }
}