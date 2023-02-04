package com.example.foody.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.foody.db.RecipesEntity
import com.example.foody.model.FoodRecipeModel
import com.example.foody.utils.NetworkResult

class RecipesBinding {

    companion object {

        @BindingAdapter("readApiResponse", "readFromDatabase", requireAll = true)
        @JvmStatic
        fun errorImageView(
            imageView: ImageView,
            apiResponse: NetworkResult<FoodRecipeModel>?,
            database: List<RecipesEntity>?
        ) {

            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()) {
                imageView.visibility = View.VISIBLE
            } else if (apiResponse is NetworkResult.Loading) {
                imageView.visibility = View.GONE

            } else if (apiResponse is NetworkResult.Success) {
                imageView.visibility = View.GONE

            }

        }

        @BindingAdapter("readApiResponse2", "readFromDatabase2", requireAll = true)
        @JvmStatic
        fun errorTextView(
            textView: TextView,
            apiResponse: NetworkResult<FoodRecipeModel>?,
            database: List<RecipesEntity>?
        ) {

            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()) {
                textView.visibility = View.VISIBLE
                textView.text = apiResponse.message.toString()
            } else if (apiResponse is NetworkResult.Loading) {
                textView.visibility = View.GONE

            } else if (apiResponse is NetworkResult.Success) {
                textView.visibility = View.GONE

            }

        }


    }

}
