package com.example.foody.di

import android.content.Context
import androidx.room.Room
import com.example.foody.db.RecipesDB
import com.example.foody.utils.Constans.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDataBase(
        @ApplicationContext context: Context
    ) =
        Room.databaseBuilder(
            context,
            RecipesDB::class.java,
            DATABASE_NAME
        ).build()

    @Singleton
    @Provides
    fun provideDao(database: RecipesDB) = database.recipesDao()
}