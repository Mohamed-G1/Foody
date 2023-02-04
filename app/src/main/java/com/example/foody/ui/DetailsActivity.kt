package com.example.foody.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.example.foody.R
import com.example.foody.adapter.PagerAdapter
import com.example.foody.databinding.ActivityDetailsBinding
import com.example.foody.ui.tabLayoutFragments.IngredientsFragment
import com.example.foody.ui.tabLayoutFragments.InstructionsFragment
import com.example.foody.ui.tabLayoutFragments.OverviewFragment

class DetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailsBinding
    private val args by navArgs<DetailsActivityArgs>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details)

        setSupportActionBar(binding.toolBar)
        binding.toolBar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initPagerAdapter()
    }

    //todo this override for handle back button in tool bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }

    private fun initPagerAdapter() {
        //todo create bundle to get the data from nav args and convey to the fragments
        val resultBundle = Bundle()
        resultBundle.putParcelable(
            "recipeBundle",
            args.result//todo because the type of args model is Parcelable
        )

        //todo create list of the fragments
        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val title = ArrayList<String>()
        title.add("Overview")
        title.add("Ingredients")
        title.add("Instructions")

        val adapter = PagerAdapter(
            resultBundle = resultBundle,
            fragments = fragments,
            title = title,
            supportFragmentManager
        )
        binding.viewPager.adapter = adapter
        binding.tapLayout.setupWithViewPager(binding.viewPager)
    }


}