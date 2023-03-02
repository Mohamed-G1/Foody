package com.example.foody.ui.fragments.recipes.bottomSheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.foody.databinding.FragmentRecipesBottomSheetBinding
import com.example.foody.ui.fragments.recipes.RecipesViewModel
import com.example.foody.utils.Constans.Companion.DEFAULT_DIET_TYPE
import com.example.foody.utils.Constans.Companion.DEFAULT_MEAL_TYPE
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.util.*


class RecipesBottomSheet : BottomSheetDialogFragment() {
    private var _binding: FragmentRecipesBottomSheetBinding? = null
    private val binding get() = _binding!!
    lateinit var recipesViewModel: RecipesViewModel


    private var mealChip = DEFAULT_MEAL_TYPE
    private var mealChipId = 0
    private var dietChip = DEFAULT_DIET_TYPE
    private var dietChipId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipesViewModel = ViewModelProvider(requireActivity())[RecipesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRecipesBottomSheetBinding.inflate(inflater, container, false)

        /**
         * to know which chips value is already exists when open bottom sheet
         * */
        recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner) { values ->
            mealChip = values.selectedMealType
            dietChip = values.selectedDietType

            updateChip(values.selectedMealTypeId, binding.mealTypeChipGroup)
            updateChip(values.selectedDietTypeId, binding.dietTypeChipGroup)

        }

        // get value from selected chips
        binding.mealTypeChipGroup.setOnCheckedStateChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId.first())
            val selectedMealType = chip.text.toString().lowercase(Locale.ROOT)
            mealChip = selectedMealType
            mealChipId = selectedChipId.first()
        }

        binding.dietTypeChipGroup.setOnCheckedStateChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId.first())
            val selectedDietType = chip.text.toString().lowercase(Locale.ROOT)
            dietChip = selectedDietType
            dietChipId = selectedChipId.first()
        }

        binding.applyBtn.setOnClickListener {
            recipesViewModel.saveMealAndDietTypeTemp(
                mealChip,
                mealChipId,
                dietChip,
                dietChipId
            )
            /**
             * this passed value is boolean because if is false will get from DB else if true will
             * request a new data*/
            val action =
                RecipesBottomSheetDirections.actionRecipesBottomSheetToRecipesFragment(true)
            findNavController().navigate(action)
        }

        return binding.root
    }


    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0) {
            try {
                val targetView = chipGroup.findViewById<Chip>(chipId)
                targetView.isChecked = true
                chipGroup.requestChildFocus(targetView, targetView)
            } catch (e: Exception) {
                Log.d("BottomSheet", e.message.toString())
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}