package com.example.foody.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.foody.R
import com.example.foody.databinding.IngredientsRowLayoutBinding
import com.example.foody.model.ExtendedIngredient
import com.example.foody.utils.Constans
import com.example.foody.utils.RecipesDiffUtil
import java.util.*

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>() {

    private var ingredientsList = emptyList<ExtendedIngredient>()
    lateinit var binding: IngredientsRowLayoutBinding

    class MyViewHolder(item: IngredientsRowLayoutBinding) :
        RecyclerView.ViewHolder(item.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = IngredientsRowLayoutBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        binding.apply {
            ingredientsImageView.load(Constans.BASE_IMAGES_URL + ingredientsList[position].image){
                crossfade(600)
                error(R.drawable.ic_error)
            }
            ingredientsTitle.text = ingredientsList[position].name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
            }
            ingredientsAmount.text = ingredientsList[position].amount.toString()
            ingredientsUnit.text = ingredientsList[position].unit
            ingredientsConsistency.text = ingredientsList[position].consistency
            ingredientsOriginal.text = ingredientsList[position].original
        }

    }

    override fun getItemCount(): Int {
        return ingredientsList.size
    }

    fun setData(newIngredient: List<ExtendedIngredient>) {
        val ingredientDiffUtil = RecipesDiffUtil(ingredientsList, newIngredient)
        val diffUtilResult = DiffUtil.calculateDiff(ingredientDiffUtil)
        ingredientsList = newIngredient
        diffUtilResult.dispatchUpdatesTo(this)
    }
}