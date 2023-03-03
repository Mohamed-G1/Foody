package com.example.foody.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foody.model.RecipesResult
import com.example.foody.utils.Constans.Companion.FAVORITE_ENTITY_TABLE


@Entity(tableName = FAVORITE_ENTITY_TABLE)
class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int ,
    var result: RecipesResult
)