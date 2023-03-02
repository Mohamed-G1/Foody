package com.example.foody.ui.tabLayoutFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.example.foody.databinding.FragmentInstructionsBinding
import com.example.foody.model.RecipesResult
import com.example.foody.utils.Constans

class InstructionsFragment : Fragment() {
    private var _binding: FragmentInstructionsBinding? = null
    private val binding get() = _binding!!

    private var myBundle: RecipesResult?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentInstructionsBinding.inflate(inflater, container, false)
        getIngredientsData()
        initUi()
        return binding.root
    }

    private fun getIngredientsData() {
        //todo get the bundle from nav args
        val args = arguments
         myBundle = args?.getParcelable(Constans.RECIPE_RESULT_KEY)
    }

    private fun initUi (){
        binding.instructionsWebView.webViewClient = object : WebViewClient(){}
        val webURL = myBundle!!.sourceUrl
        binding.instructionsWebView.loadUrl(webURL)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}