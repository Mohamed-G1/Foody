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
    lateinit var binding : FragmentRecipesBottomSheetBinding
    lateinit var recipesViewModel: RecipesViewModel


    private var mealChip = DEFAULT_MEAL_TYPE
    private var mealChipId = 0
    private var dietChip = DEFAULT_DIET_TYPE
    private var dietChipId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRecipesBottomSheetBinding.inflate(inflater, container, false)

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
        binding.mealTypeChipGroup.setOnCheckedChangeListener() { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedMealType = chip.text.toString().lowercase(Locale.getDefault())
            mealChip = selectedMealType
            mealChipId = selectedChipId
        }

        binding.dietTypeChipGroup.setOnCheckedChangeListener() { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedDietType = chip.text.toString().lowercase(Locale.getDefault())
            dietChip = selectedDietType
            dietChipId = selectedChipId
        }

        binding.applyBtn.setOnClickListener {
            recipesViewModel.saveMealAndDietType(
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

    private fun updateChip(selectedMealTypeId: Int, chipGroup: ChipGroup) {
        if (selectedMealTypeId != 0) {
            try {
                chipGroup.findViewById<Chip>(selectedMealTypeId).isChecked = true
            } catch (e: Exception) {
                Log.d("BottomSheet", e.message.toString())
            }

        }
    }


}