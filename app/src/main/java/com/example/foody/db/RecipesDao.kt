package com.example.foody.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foody.db.entities.FavoriteEntity
import com.example.foody.db.entities.FoodJokeEntity
import com.example.foody.db.entities.RecipesEntity
import com.example.foody.model.FoodJoke
import kotlinx.coroutines.flow.Flow
import retrofit2.http.DELETE


@Dao
interface RecipesDao {
    /**
     * this for recipes screen*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipesEntity: RecipesEntity)

    // this to read all data from entity
    @Query("SELECT * FROM recipes_table ORDER BY id ASC")
    fun readRecipes(): Flow<List<RecipesEntity>>


    /**
     * this for favorite screen*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteRecipe(favoriteEntity: FavoriteEntity)

    @Query("SELECT * FROM favorite_table ORDER BY id ASC")
    fun readFavoriteRecipe(): Flow<List<FavoriteEntity>>

    @Delete
    suspend fun deleteFavoriteRecipe(favoriteEntity: FavoriteEntity)

    @Query("DELETE FROM favorite_table")
    suspend fun deleteAllFavoriteRecipe()

    /**
     * this for food joke screen*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodJoke(foodJoke: FoodJokeEntity)

    @Query("SELECT * FROM food_joke_table ORDER BY id ASC")
    fun readFoodJoke(): Flow<List<FoodJokeEntity>>


}