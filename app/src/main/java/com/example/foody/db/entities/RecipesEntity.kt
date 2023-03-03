package com.example.foody.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foody.model.FoodRecipeModel
import com.example.foody.utils.Constans.Companion.RECIPES_TABLE


@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(
    var foodRecipeModel: FoodRecipeModel
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}