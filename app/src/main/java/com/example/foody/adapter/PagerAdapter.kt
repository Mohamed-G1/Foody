package com.example.foody.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PagerAdapter(
    private val resultBundle: Bundle, //todo to parse the data from details activity to tab layout fragments
    private val fragments: ArrayList<Fragment>, // todo but tap layout fragments in an array list
    private val title: ArrayList<String>,
    fm: FragmentManager // todo fragment manager that will interact with this adapter
) : FragmentPagerAdapter(
    fm,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) // todo this behavior for what adapter should handle the fragments
{
    override fun getCount(): Int {
        //todo return the number of fragments
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        //todo pass all model to those fragments
        fragments[position].arguments = resultBundle
        return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence {
        //todo to return the titles of those fragments
        return title[position]
    }
}