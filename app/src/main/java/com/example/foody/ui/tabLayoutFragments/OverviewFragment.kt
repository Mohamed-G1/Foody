package com.example.foody.ui.tabLayoutFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.parseAsHtml
import coil.load
import com.example.foody.R
import com.example.foody.bindingadapters.RecipesRowBinding
import com.example.foody.bindingadapters.RecipesRowBinding.Companion.parseHtml
import com.example.foody.databinding.FragmentOverviewBinding
import com.example.foody.databinding.RecipesRowLayoutBinding
import com.example.foody.model.RecipesResult
import com.example.foody.utils.Constans
import org.jsoup.Jsoup


class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        initViews()
        return binding.root

    }

    private fun initViews() {
        //todo get the bundle from nav args
        val args = arguments
        val myBundle: RecipesResult =
            args!!.getParcelable<RecipesResult>(Constans.RECIPE_RESULT_KEY) as RecipesResult
        binding.apply {
            mainImageView.load(myBundle.image)
            titleTextView.text = myBundle.title
            likesTextView.text = myBundle.aggregateLikes.toString()
            timeTextView.text = myBundle.readyInMinutes.toString()
            //todo this to parse the Html tags that appears when shows the data
//            myBundle.summary.let {
//                val summary = Jsoup.parse(it).text()
//                summeryTextView.text = summary
//            }
            //todo Or we can cleanup the up function into this line
//            RecipesRowLayoutBinding.parseHtml()
            parseHtml(binding.summeryTextView, myBundle.summary)


        }

        updateColors(myBundle.vegetarian, binding.vegetarianTextView, binding.vegetarianImageView)
        updateColors(myBundle.vegan, binding.veganTextView, binding.veganImageView)
        updateColors(myBundle.cheap, binding.cheapTextView, binding.cheapImageView)
        updateColors(myBundle.dairyFree, binding.dairyfreeTextView, binding.dairyfreeImageView)
        updateColors(myBundle.glutenFree, binding.glutenTextView, binding.glutenImageView)
        updateColors(myBundle.veryHealthy, binding.heathTextView, binding.healthImageView)

    }

    private fun updateColors(stateIsOn: Boolean, textView: TextView, imageView: ImageView) {
        if (stateIsOn) {
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            imageView.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}