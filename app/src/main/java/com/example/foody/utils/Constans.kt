package com.example.foody.utils

class Constans {

    companion object {

        const val BASE_URL = "https://api.spoonacular.com"
        //todo Endpoints like the ingredient autosuggestion will only give you an image name.
        // You have to build the full URL by adding the base path to the beginning
        const val BASE_IMAGES_URL = "https://spoonacular.com/cdn/ingredients_100x100/"


        // room database
        const val DATABASE_NAME = "recipes_database"
        const val RECIPES_TABLE = "recipes_table"
        const val FOOD_JOKE_TABLE = "food_joke_table"
        const val FAVORITE_ENTITY_TABLE = "favorite_table"

        // bottom sheet and preferences
        const val DEFAULT_MEAL_TYPE = "main course"
        const val DEFAULT_DIET_TYPE = "gluten free"
        const val DEFAULT_RECIPES_NUMBER = "50"

        const val PREFERENCES_NAME = "foody_preferences"
        const val PREFERENCES_MEAL_TYPE = "mealType"
        const val PREFERENCES_MEAL_TYPE_ID = "mealTypeId"
        const val PREFERENCES_DIET_TYPE = "dietType"
        const val PREFERENCES_DIET_TYPE_ID = "dietTypeId"

        const val PREFERENCES_BACK_ONLINE = "backOnLine"
        const val QUERY_SEARCH = "query"

        const val RECIPE_RESULT_KEY = "recipeBundle"


    }
}