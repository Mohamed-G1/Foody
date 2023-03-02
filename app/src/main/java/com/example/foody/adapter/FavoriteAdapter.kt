package com.example.foody.adapter

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.foody.R
import com.example.foody.databinding.FavoriteRecipeRowLayoutBinding
import com.example.foody.db.entities.FavoriteEntity
import com.example.foody.ui.MainViewModel
import com.example.foody.ui.fragments.favorites.FavoriteRecipesFragmentDirections
import com.example.foody.utils.RecipesDiffUtil
import com.google.android.material.snackbar.Snackbar

class FavoriteAdapter(
    private val fragmentActivity: FragmentActivity,
    private val mainViewModel: MainViewModel,
    private val showSnackBar : (String) -> Unit
) :
    RecyclerView.Adapter<FavoriteAdapter.MyViewHolder>(), ActionMode.Callback {

    var isMultiSelected = false //todo to check is multi section or not
    var selectedRecipes = arrayListOf<FavoriteEntity>() //todo to store the selected recipes
    private var mViewHolder = arrayListOf<MyViewHolder>()

    private lateinit var mActionMode: ActionMode //todo this to use in close action mode whenever no selection items

    private var favoriteEntity = emptyList<FavoriteEntity>()

    class MyViewHolder(val binding: FavoriteRecipeRowLayoutBinding) :
        ViewHolder(binding.root) {
        val rootLayout = binding.favoriteRecipesRowLayout
        val favoriteCardView = binding.favoriteRowCardView
        fun bind(favoriteEntity: FavoriteEntity) {
            binding.favoriteEntity = favoriteEntity
            binding.executePendingBindings() //todo this to update the date wih views
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoriteRecipeRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        mViewHolder.add(holder)
        val currentRecipe = favoriteEntity[position]
        holder.bind(currentRecipe)

        saveItemStateOnScroll(currentRecipe, holder)

        /**
         * Single Click Listener
         * this to navigate to details with the selected recipe
         * */
        holder.rootLayout.setOnClickListener {
            if (isMultiSelected) {
                applySelection(holder, currentRecipe)
            } else {
                val action =
                    FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToDetailsActivity(
                        result = currentRecipe.result
                    )
                holder.rootLayout.findNavController().navigate(action)
            }

        }

        /**
         * Long Click Listener
         * this to show Action Mode to can delete the selected recipe
         * */
        holder.rootLayout.setOnLongClickListener {
            if (!isMultiSelected) {
                isMultiSelected = true
                holder.itemView.startActionMode(this)
                applySelection(holder, currentRecipe)
                true
            } else {
                applySelection(holder, currentRecipe)
                true
            }

        }

    }

    override fun getItemCount(): Int {
        return favoriteEntity.size
    }


    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.favorite_contextual_menu, menu)
        mActionMode = mode!!
        applyStatusBarColor(R.color.contextual_action_mode_color)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if (item?.itemId == R.id.deleteFavoriteRecipe) {
            //todo this to loop on all selected and remove them
            selectedRecipes.forEach { listItems ->
                mainViewModel.deleteFavoriteRecipe(listItems)
            }
            showSnackBar("${selectedRecipes.size} recipe/s deleted")
            isMultiSelected = false
            selectedRecipes.clear()
            mActionMode.finish()
        }
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        //todo to ensure that we clear the list after exit from action mode
        isMultiSelected = false
        selectedRecipes.clear()
        //todo this to loop on all selection items and return every selected item to the default color is case we exit during selection
        mViewHolder.forEach {
            changeRecipeStyle(
                it,
                backgroundColor = R.color.white,
                strokeColor = R.color.mediumGray
            )
        }
        applyStatusBarColor(R.color.colorPrimary)
    }

    private fun applyStatusBarColor(color: Int) {
        fragmentActivity.window.statusBarColor = ContextCompat.getColor(fragmentActivity, color)
    }

    /**
     * this to avoid the default scrolling recycler that occur for example when we try to select first item
     * in a list from 10 items and scrolled down we found the 8th item automatic selected,
     * so to avoid that we need to save recycler view state
     * */
    private fun saveItemStateOnScroll(currentRecipes:FavoriteEntity, holder: MyViewHolder){
        if (selectedRecipes.contains(currentRecipes)) {
            changeRecipeStyle(holder, backgroundColor = R.color.strokeColor, strokeColor = R.color.colorPrimary)
        } else {
            changeRecipeStyle(holder, backgroundColor = R.color.white, strokeColor = R.color.mediumGray)
        }
    }

    /**
     * this to put or to remove that's in selected items in empty array list
     */
    private fun applySelection(holder: MyViewHolder, currentRecipes: FavoriteEntity) {
        //todo that's mean if you press again after selected the item will remove it from the array list
        if (selectedRecipes.contains(currentRecipes)) {
            selectedRecipes.remove(currentRecipes)
            changeRecipeStyle(
                holder,
                backgroundColor = R.color.white,
                strokeColor = R.color.mediumGray
            )
            applyActionModeTitle()
        } else {
            selectedRecipes.add(currentRecipes)
            changeRecipeStyle(
                holder,
                backgroundColor = R.color.strokeColor,
                strokeColor = R.color.colorPrimary
            )
            applyActionModeTitle()
        }
    }

    /** this will use when remove selection from last item the action mode will exit autmatically*/
    private fun applyActionModeTitle() {
        when (selectedRecipes.size) {
            //todo that's mean no selection items then finish the action mode
            0 -> {
                mActionMode.finish()
                isMultiSelected = false
            }
            //todo this to counted the items and showing in title
            1 -> {
                mActionMode.title = "${selectedRecipes.size} items selected"
            }
            //todo this also to calculate the items after 1
            else -> {
                mActionMode.title = "${selectedRecipes.size} items selected"

            }
        }
    }

    /**
     * this fun to change recipe style when selection
     * */
    private fun changeRecipeStyle(holder: MyViewHolder, backgroundColor: Int, strokeColor: Int) {
        holder.rootLayout.setBackgroundColor(
            ContextCompat.getColor(
                holder.itemView.context,
                backgroundColor
            )
        )
        holder.favoriteCardView.strokeColor =
            ContextCompat.getColor(holder.itemView.context, strokeColor)

    }

    fun setData(newFavoriteEntity: List<FavoriteEntity>) {
        val favoriteDiffUtil = RecipesDiffUtil(favoriteEntity, newFavoriteEntity)
        val diffUtiResult = DiffUtil.calculateDiff(favoriteDiffUtil)
        favoriteEntity = newFavoriteEntity
        diffUtiResult.dispatchUpdatesTo(this)
    }

    /** this to close the action mode if its visible when navigate during its running
     * */
    fun clearActionMode(){
        if (this::mActionMode.isInitialized)
            mActionMode.finish()
    }


}