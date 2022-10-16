package com.example.foody.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.foody.model.RecipesResult

class RecipesDiffUtil(
    private val oldList: List<RecipesResult>,
    private val newList: List<RecipesResult>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // this === to compare with the two lists
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}