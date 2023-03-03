package com.example.foody.ui.fragments.favorites

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.example.foody.R
import com.example.foody.adapter.FavoriteAdapter
import com.example.foody.databinding.FragmentFavoriteRecipesBinding
import com.example.foody.ui.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment() {
    private var _binding: FragmentFavoriteRecipesBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()
    private val mAdapter: FavoriteAdapter by lazy {
        FavoriteAdapter(requireActivity(), mainViewModel) { message ->
            showSnackBar(message)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteRecipesBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        binding.mAdapter = mAdapter
        initUi()
        initMenuBar()
        return binding.root
    }

    private fun initMenuBar() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.favorite_delete_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.favoriteDeleteAll) {
                    mainViewModel.deleteAllFavoriteRecipe()
                    if (mainViewModel.readFavoriteRecipe.value.isNullOrEmpty()) showSnackBar("No recipes to remove.") else showSnackBar(
                        "All recipes removed."
                    )

                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun initUi() {
        binding.favoriteRecyclerView.adapter = mAdapter
    }


    @SuppressLint("ShowToast")
    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).setAction("Okay") {
        }.show()
    }

    override fun onDetach() {
        super.onDetach()
        _binding = null
        mAdapter.clearActionMode()
    }

}