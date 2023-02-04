package com.example.foody.ui.tabLayoutFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import coil.load
import com.example.foody.R
import com.example.foody.databinding.FragmentOverviewBinding
import com.example.foody.model.RecipesResult
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
        val myBundle: RecipesResult? = args?.getParcelable("recipeBundle")
        binding.apply {
            mainImageView.load(myBundle?.image)
            titleTextView.text = myBundle?.title
            likesTextView.text = myBundle?.aggregateLikes.toString()
            timeTextView.text = myBundle?.readyInMinutes.toString()
            //todo this to parse the Html tags that appears when shows the data
            myBundle?.summary.let {
                val summary = Jsoup.parse(it).text()
                summeryTextView.text = summary
            }

        }
        if (myBundle?.vegetarian == true) {
            binding.apply {
                vegetarianImageView.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.green
                    )
                )
                vegetarianTextView.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.green
                    )
                )
            }
        }

        if (myBundle?.vegan == true) {
            binding.apply {
                veganImageView.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.green
                    )
                )
                veganTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            }
        }

        if (myBundle?.glutenFree == true) {
            binding.apply {
                glutenImageView.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.green
                    )
                )
                glutenTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            }
        }

        if (myBundle?.dairyFree == true) {
            binding.apply {
                dairyfreeImageView.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.green
                    )
                )
                dairyfreeTextView.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.green
                    )
                )
            }
        }

        if (myBundle?.veryHealthy == true) {
            binding.apply {
                healthImageView.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.green
                    )
                )
                heathTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            }
        }

        if (myBundle?.cheap == true) {
            binding.apply {
                cheapImageView.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.green
                    )
                )
                cheapTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            }
        }

    }

    //todo for covert color of checkmark into green in true
    fun handleCheckmarksColor() {
    }
}