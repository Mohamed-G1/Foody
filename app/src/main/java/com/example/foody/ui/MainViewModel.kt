package com.example.foody.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.data.Repository
import com.example.foody.model.FoodRecipeModel
import com.example.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: Repository,
    application: Application
) : AndroidViewModel(application) {

    var recipesResponse: MutableLiveData<NetworkResult<FoodRecipeModel>> = MutableLiveData()


    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    // check internet before hit the api
    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repo.remote.getRecipes(queries)
                recipesResponse.value = handleFoodRecipesResponse(response)

            } catch (e: Exception) {
                recipesResponse.value = NetworkResult.Error("Recipes Not Found.")
            }
        } else {
            recipesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    // this to parse and handle response from api
    private fun handleFoodRecipesResponse(response: Response<FoodRecipeModel>): NetworkResult<FoodRecipeModel>? {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Request Time Out.")
            }
            // bcz the our api has a limit api key so may return with this error code
            response.code() == 402 -> {
                return NetworkResult.Error("Api Key Limited")
            }
            // some times you get result from api but the result is empty
            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Recipes Not Found.")
            }
            // handle the success
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }

    }

    // check internet connection
    fun hasInternetConnection(): Boolean {
        val connectivityManger = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManger.activeNetwork ?: return false
        val capabilities = connectivityManger.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}