package com.example.foody.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.example.foody.db.entities.RecipesEntity
import com.example.foody.model.FoodRecipeModel
import com.example.foody.utils.NetworkResult

class RecipesBinding {

    companion object {

        @BindingAdapter("readApiResponse", "readFromDatabase", requireAll = true)
        @JvmStatic
        fun errorImageView(
            view: View,
            apiResponse: NetworkResult<FoodRecipeModel>?,
            database: List<RecipesEntity>?
        ) {
            when (view) {
                is ImageView -> {
                    view.isVisible = database.isNullOrEmpty() && apiResponse is NetworkResult.Error
                }
                is TextView -> {
                    view.isVisible = database.isNullOrEmpty() && apiResponse is NetworkResult.Error
                    view.text = apiResponse?.message.toString()
                }


            }
        }


    }

}
