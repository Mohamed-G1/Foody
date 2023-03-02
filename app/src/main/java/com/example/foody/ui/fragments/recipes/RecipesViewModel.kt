package com.example.foody.ui.fragments.recipes

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.foody.BuildConfig
import com.example.foody.data.DataStoreRepository
import com.example.foody.data.MealAndDietType
import com.example.foody.utils.Constans.Companion.DEFAULT_DIET_TYPE
import com.example.foody.utils.Constans.Companion.DEFAULT_MEAL_TYPE
import com.example.foody.utils.Constans.Companion.DEFAULT_RECIPES_NUMBER
import com.example.foody.utils.Constans.Companion.QUERY_SEARCH
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {
    // this varibles to change request in queries
    var networkStatus = false
    var backOnLine = false
    val readMealAndDietType = dataStoreRepository.readMealAndDiet
    val readBackOnLine = dataStoreRepository.readBackOnlineValue.asLiveData()
    lateinit var mealAndDietType: MealAndDietType


    fun saveMealAndDietType() =
        viewModelScope.launch(Dispatchers.IO) {
            if (this@RecipesViewModel::mealAndDietType.isInitialized) {
                dataStoreRepository.saveMealAndDiet(
                    mealAndDietType.selectedMealType,
                    mealAndDietType.selectedMealTypeId,
                    mealAndDietType.selectedDietType,
                    mealAndDietType.selectedDietTypeId
                )
            }
        }

    /**
     * this to if we received recipes not found then we didn't save the value of chides
     * and stays the chides value temporoy and save it just in success in recipres fragment
     * */
    fun saveMealAndDietTypeTemp(
        mealType: String,
        mealTypeId: Int,
        dietType: String,
        dietTypeId: Int
    ) {
        mealAndDietType = MealAndDietType(
            mealType, mealTypeId, dietType, dietTypeId
        )
    }

    private fun saveBackOnLineValue(backOnLine: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveBackOnlineValue(backOnLine = backOnLine)
        }

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        /**
         * this way to get the newest values inside our apply queries every time
         * */
        queries["number"] = DEFAULT_RECIPES_NUMBER
        queries["apiKey"] = BuildConfig.API_KEY
        queries["addRecipeInformation"] = "true"
        queries["fillIngredients"] = "true"

        /**
         * this to ensure that the MealAndType is inilaizined first*/
        if (this@RecipesViewModel::mealAndDietType.isInitialized) {
            queries["type"] = mealAndDietType.selectedMealType
            queries["diet"] = mealAndDietType.selectedDietType
        } else {
            queries["type"] = DEFAULT_MEAL_TYPE
            queries["diet"] = DEFAULT_DIET_TYPE
        }
        return queries
    }

    fun applySearchQueries(querySearch: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_SEARCH] = querySearch
        queries["number"] = DEFAULT_RECIPES_NUMBER
        queries["apiKey"] = BuildConfig.API_KEY
        queries["addRecipeInformation"] = "true"
        queries["fillIngredients"] = "true"
        return queries
    }

    fun showNetworkStatus() {
        // mean if network is not avalible
        if (!networkStatus) {
            Toast.makeText(getApplication(), "No Internet Connection", Toast.LENGTH_SHORT).show()
            saveBackOnLineValue(true)
            // mean if network is avalible
        } else if (networkStatus) {
            if (backOnLine) {
                Toast.makeText(getApplication(), "Back Online", Toast.LENGTH_SHORT).show()
                saveBackOnLineValue(false)
            }

        }
    }
}