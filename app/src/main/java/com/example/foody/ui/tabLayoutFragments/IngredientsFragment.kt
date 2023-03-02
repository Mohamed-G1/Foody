package com.example.foody.ui.tabLayoutFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foody.adapter.IngredientsAdapter
import com.example.foody.databinding.FragmentIngredientsBinding
import com.example.foody.model.RecipesResult
import com.example.foody.utils.Constans

class IngredientsFragment : Fragment() {

    private var _binding: FragmentIngredientsBinding? = null
    private val binding get() = _binding!!

    private val ingredientsAdapter: IngredientsAdapter by lazy { IngredientsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentIngredientsBinding.inflate(inflater, container, false)

        setRecyclerView()

        getIngredientsData()

        return binding.root
    }

    private fun getIngredientsData() {
        //todo get the bundle from nav args
        val args = arguments
        val myBundle: RecipesResult? = args?.getParcelable(Constans.RECIPE_RESULT_KEY)
        myBundle?.extendedIngredients?.let { ingredientsAdapter.setData(it) }
    }

    private fun setRecyclerView() {
        binding.ingredientsRecyclerView.adapter = ingredientsAdapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}