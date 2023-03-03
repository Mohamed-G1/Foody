package com.example.foody.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(
    private val resultBundle: Bundle, //todo to parse the data from details activity to tab layout fragments
    private val fragments: ArrayList<Fragment>, // todo but tap layout fragments in an array list
    fm: FragmentActivity // todo fragment manager that will interact with this adapter
) : FragmentStateAdapter(fm) // todo this behavior for what adapter should handle the fragments
{
    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment {
        //todo to return the titles of those fragments
        //todo pass all model to those fragments
        fragments[position].arguments = resultBundle
        return fragments[position]
    }
}