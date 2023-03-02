package com.example.foody.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foody.adapter.FavoriteAdapter
import com.example.foody.db.entities.FavoriteEntity

class FavoriteBindingAdapter {
    companion object {
        @BindingAdapter("setVisibility", "setData", requireAll = false)
        @JvmStatic
        fun setDataAndVisibility(
            view: View,
            favoriteEntity: List<FavoriteEntity>?,
            mAdapter: FavoriteAdapter?
        ) {
            when (view) {
                is RecyclerView -> {
                    view.isInvisible = favoriteEntity.isNullOrEmpty()
                    if (!favoriteEntity.isNullOrEmpty())
                        favoriteEntity.let {
                            mAdapter?.setData(it)
                        }
                }
                else -> view.isVisible = favoriteEntity.isNullOrEmpty()
            }
        }

    }
}