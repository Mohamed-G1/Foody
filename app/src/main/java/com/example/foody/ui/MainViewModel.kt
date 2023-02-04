package com.example.foody.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.example.foody.data.Repository
import com.example.foody.db.RecipesEntity
import com.example.foody.model.FoodRecipeModel
import com.example.foody.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: Repository,
    application: Application
) : AndroidViewModel(application) {

    /** CACHE DB*/
    val readRecipes: LiveData<List<RecipesEntity>> = repo.local.readDatabase().asLiveData()
    private fun insertRecipes(recipesEntity: RecipesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repo.local.insertRecipes(recipesEntity)
        }

    /** RETROFIT */
    var recipesResponse: MutableLiveData<NetworkResult<FoodRecipeModel>> = MutableLiveData()
    var searchRecipesResponse : MutableLiveData<NetworkResult<FoodRecipeModel>> = MutableLiveData()

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    fun getSearchRecipes(searchQueries: Map<String, String>) = viewModelScope.launch {
        getSearchRecipesSafeCall(searchQueries)
    }


    // check internet before hit the api
    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repo.remote.getRecipes(queries)
                recipesResponse.value = handleFoodRecipesResponse(response)

                /** CACHE DB*/
                val foodRecipes = recipesResponse.value!!.data
                if (foodRecipes != null) {
                    offlineCacheRecipes(foodRecipes)
                }
            } catch (e: Exception) {
                recipesResponse.value = NetworkResult.Error("Recipes Not Found.")
            }
        }
        else {
            recipesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private suspend fun getSearchRecipesSafeCall(searchQueries: Map<String, String>) {
        searchRecipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()){
            try {
                val response = repo.remote.searchRecipes(searchQueries)
                searchRecipesResponse.value = handleFoodRecipesResponse(response)

            }catch (e: Exception){
                searchRecipesResponse.value = NetworkResult.Error("Not Found")
            }
        }else{
            recipesResponse.value = NetworkResult.Error("No Internet Connection.")

        }
    }


    private fun offlineCacheRecipes(foodRecipes: FoodRecipeModel) {
        val recipesEntity = RecipesEntity(foodRecipes)
        insertRecipes(recipesEntity)
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
    private fun hasInternetConnection(): Boolean {
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