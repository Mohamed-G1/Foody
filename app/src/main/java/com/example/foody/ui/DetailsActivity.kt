package com.example.foody.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navArgs
import com.example.foody.R
import com.example.foody.adapter.PagerAdapter
import com.example.foody.databinding.ActivityDetailsBinding
import com.example.foody.db.entities.FavoriteEntity
import com.example.foody.ui.tabLayoutFragments.IngredientsFragment
import com.example.foody.ui.tabLayoutFragments.InstructionsFragment
import com.example.foody.ui.tabLayoutFragments.OverviewFragment
import com.example.foody.utils.Constans
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailsBinding
    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel: MainViewModel by viewModels()

    var isSavedRecipe = false
    var savedRecipeID = 0
    private lateinit var menuItem : MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolBar)
        binding.toolBar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initPagerAdapter()

    }

    //todo this to show the star menu icon in menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
         menuItem = menu!!.findItem(R.id.saveToFavorite)
        checkSavedRecipes(menuItem)
        return true
    }


    //todo this override for handle any menu items in tool bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        else if (item.itemId == R.id.saveToFavorite && !isSavedRecipe) //todo mean if isSaved == false
            saveToFavorite(item)
        else if (item.itemId == R.id.saveToFavorite && isSavedRecipe) //todo mean if isSaved == true
            deleteFavoriteRecipe(item)
        return super.onOptionsItemSelected(item)
    }


    //todo to check if the recipe is saved keep star color yellow else not saved keep the white color
    private fun checkSavedRecipes(menuItem: MenuItem) {
        mainViewModel.readFavoriteRecipe.observe(this) {
            try {
                for (savedRecipe in it) {
                    if (savedRecipe.result.id == args.result.id) {
                        changeMenuItemColor(menuItem, R.color.yellow)
                        savedRecipeID = savedRecipe.id
                        isSavedRecipe = true
                    }
                }
            } catch (e: Exception) {
                Log.d("DetailsActivity", e.message.toString())
            }
        }
    }

    private fun deleteFavoriteRecipe(item: MenuItem) {
        val favoriteEntity = FavoriteEntity(savedRecipeID, args.result)
        mainViewModel.deleteFavoriteRecipe(favoriteEntity = favoriteEntity)
        changeMenuItemColor(item, R.color.white)
        showSnackBar("Recipe Deleted.")
        isSavedRecipe = false
    }


    //todo use favorite entity to save the recipe
    private fun saveToFavorite(item: MenuItem) {
        val favoriteEntity = FavoriteEntity(0, args.result)
        mainViewModel.insertFavoriteRecipe(favoriteEntity = favoriteEntity)
        changeMenuItemColor(item, R.color.yellow)
        showSnackBar("Recipe Saved.")
        isSavedRecipe = true
    }

    private fun showSnackBar(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    //todo change icon color when save to DB
    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon?.setTint(ContextCompat.getColor(this, color))
    }

    private fun initPagerAdapter() {
        //todo create list of the fragments
        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val title = ArrayList<String>()
        title.add("Overview")
        title.add("Ingredients")
        title.add("Instructions")

        //todo create bundle to get the data from nav args and convey to the fragments
        val resultBundle = Bundle()
        lifecycleScope.launch {
            resultBundle.putParcelable(
                Constans.RECIPE_RESULT_KEY,
                args.result//todo because the type of args model is Parcelable
            )
        }



        val adapter = PagerAdapter(
            resultBundle = resultBundle,
            fragments = fragments,
            this
        )
        binding.apply {
            /**this line to moving to the fragments from clicks on tab layout not from swiping*/
            viewPager.isUserInputEnabled = false
            viewPager.adapter = adapter
            TabLayoutMediator(binding.tapLayout, binding.viewPager ){ tab , position ->
                tab.text = title[position]
            }.attach()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //todo to change the color icon just whenever close the activity
        changeMenuItemColor(menuItem, R.color.white)
    }


}