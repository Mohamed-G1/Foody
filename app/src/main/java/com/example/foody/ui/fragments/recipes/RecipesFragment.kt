package com.example.foody.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foody.R
import com.example.foody.adapter.RecipesAdapter
import com.example.foody.databinding.FragmentRecipesBinding
import com.example.foody.ui.MainViewModel
import com.example.foody.utils.NetworkListener
import com.example.foody.utils.NetworkResult
import com.example.foody.utils.observeOnce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RecipesFragment : Fragment(), SearchView.OnQueryTextListener {
    private val args by navArgs<RecipesFragmentArgs>()
    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    lateinit var mViewModel: MainViewModel
    lateinit var recipesViewModel: RecipesViewModel
    private val mAdapter by lazy { RecipesAdapter() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        recipesViewModel = ViewModelProvider(requireActivity())[RecipesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this // bez we'll use live data in this fragment
        binding.mainViewModel = mViewModel

        initMenuBar()
        observeOnBackOnlineValue()
        setupRecyclerView()
        initViewClicks()
        networkListener()

        return binding.root
    }

    private fun networkListener(){
        /**
         * launchWhenStarted ->  stateflow is working on all the screens of the app so
         * to get the value from the stateflow just when the fragment started not all the app
         * */
        lifecycleScope.launchWhenStarted {
            val networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext()).collect { status ->
                Log.d("NetworkStatus", status.toString())
                recipesViewModel.networkStatus = status
                recipesViewModel.showNetworkStatus()
                readDatabase()
            }
        }
    }

    private fun initViewClicks(){
        binding.recipesFab.setOnClickListener {
            if (recipesViewModel.networkStatus) {
                findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
            } else {
                recipesViewModel.showNetworkStatus()
            }
        }
    }


    private fun initMenuBar() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.recipes_search, menu)
                val search = menu.findItem(R.id.search_menu)
                val searchView = search.actionView as? SearchView
                searchView?.isSubmitButtonEnabled = true
                searchView?.setOnQueryTextListener(this@RecipesFragment)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null)
            searchApiData(searchQuery = query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    private fun searchApiData(searchQuery: String) {
        showShimmer()
        mViewModel.getSearchRecipes(recipesViewModel.applySearchQueries(searchQuery))
        mViewModel.searchRecipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmer()
                    val foodRecipe = response.data
                    foodRecipe?.let {
                        mAdapter.setData(it)
                    }
                }
                is NetworkResult.Error -> {
                    hideShimmer()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    showShimmer()
                }
            }

        }
    }

    private fun setupRecyclerView() {
        binding.recipesRecyclerView.adapter = mAdapter
        binding.recipesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        showShimmer()
    }

    private fun observeOnBackOnlineValue() {
        recipesViewModel.readBackOnLine.observe(viewLifecycleOwner) {
            recipesViewModel.backOnLine = it
        }
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mViewModel.readRecipes.observeOnce(this@RecipesFragment, Observer { database ->
                if (database.isNotEmpty() && !args.backFromBottomSheet) {
                    Log.d("RecipeFragment", "readDatabase")
                    mAdapter.setData(database.first().foodRecipeModel)
                    hideShimmer()
                } else {
                    requestApiData()
                }
            })
        }
    }

    private fun requestApiData() {
        Log.d("RecipeFragment", "requestNewApi")
        mViewModel.getRecipes(recipesViewModel.applyQueries())
        mViewModel.recipesResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmer()
                    response.data?.let { mAdapter.setData(it) }
                    // todo to save the chides values just if in success
                    recipesViewModel.saveMealAndDietType()
                }

                is NetworkResult.Error -> {
                    hideShimmer()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    showShimmer()
                }

            }

        })
    }


    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mViewModel.readRecipes.observe(viewLifecycleOwner, Observer { database ->
                if (database.isNotEmpty()) {
                    mAdapter.setData(database.first().foodRecipeModel)
                }
            })
        }
    }


    private fun showShimmer() {
        binding.apply {
            shimmerFrameLayout.startShimmer()
            shimmerFrameLayout.visibility = View.VISIBLE
            recipesRecyclerView.visibility = View.GONE
        }
    }

    private fun hideShimmer() {
        binding.apply {
            shimmerFrameLayout.stopShimmer()
            shimmerFrameLayout.visibility = View.GONE
            recipesRecyclerView.visibility = View.VISIBLE
        }
    }


    /**
     * this for memory leak
     * */
    override fun onDestroy() {
        super.onDestroy()
        mViewModel.recyclerViewState =
            binding.recipesRecyclerView.layoutManager?.onSaveInstanceState()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        /** this very useful when u want to navigate and return at the same recycler view position*/
        if (mViewModel.recyclerViewState != null) {
            binding.recipesRecyclerView.layoutManager?.onRestoreInstanceState(mViewModel.recyclerViewState)
        }
    }

}