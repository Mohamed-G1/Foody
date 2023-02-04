package com.example.foody.ui.fragments.recipes

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.foody.BuildConfig
import com.example.foody.data.DataStoreRepository
import com.example.foody.utils.Constans
import com.example.foody.utils.Constans.Companion.DEFAULT_DIET_TYPE
import com.example.foody.utils.Constans.Companion.DEFAULT_MEAL_TYPE
import com.example.foody.utils.Constans.Companion.DEFAULT_RECIPES_NUMBER
import com.example.foody.utils.Constans.Companion.QUERY_SEARCH
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {
    // this varibles to change request in queries
    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE
    var networkStatus = false
    var backOnLine = false

    val readMealAndDietType = dataStoreRepository.readMealAndDiet
    val readBackOnLine = dataStoreRepository.readBackOnlineValue.asLiveData()

    fun saveMealAndDietType(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMealAndDiet(mealType, mealTypeId, dietType, dietTypeId)
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
        viewModelScope.launch {
            //collect() -> to collect values from flow
            readMealAndDietType.collect { values ->
                mealType = values.selectedMealType
                dietType = values.selectedDietType

            }
        }

        queries["number"] = DEFAULT_RECIPES_NUMBER
        queries["apiKey"] = BuildConfig.API_KEY
        queries["type"] = mealType
        queries["diet"] = dietType
        queries["addRecipeInformation"] = "true"
        queries["fillIngredients"] = "true"
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