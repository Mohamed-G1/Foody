package com.example.foody.db.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foody.model.FoodJoke
import com.example.foody.utils.Constans.Companion.FOOD_JOKE_TABLE


@Entity(tableName = FOOD_JOKE_TABLE)
class FoodJokeEntity(
    /**
     * we can use Embedded if the stored model not complex model that's mean we didn't need to use TypeConverter
     * so in this case the model (FoodJokeModel) just has one string parameter
     * so Embedded do inspect on that model and extract the field
     */
    @Embedded
    var foodJoke: FoodJoke
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}