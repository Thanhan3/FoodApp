package com.example.foodapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentSearchBinding
import com.example.foodapp.ui.favorite.adapter.MealAdapter
import com.example.foodapp.ui.home.viewmodel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {
    private lateinit var binding :FragmentSearchBinding
    private val viewModel : HomeViewModel by activityViewModels()
    private lateinit var searchRecycleViewAdapter : MealAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecycleView()
        binding.imgSearch.setOnClickListener {
            searchMeal()
        }
        observeSearchMeal()
        var searchJob : Job? = null
        binding.edSearchBox.addTextChangedListener { searchQuery ->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(500)
                viewModel.searchMeal(searchQuery.toString())
            }
        }
    }

    private fun observeSearchMeal() {
        viewModel.searchMealLiveData.observe(viewLifecycleOwner){
            searchRecycleViewAdapter.differ.submitList(it)
        }
    }

    private fun searchMeal() {
        val query = binding.edSearchBox.text.toString()
        if (query.isNotEmpty()){
            viewModel.searchMeal(query)
        }

    }

    private fun prepareRecycleView() {
        searchRecycleViewAdapter = MealAdapter()
        binding.rvResult.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter= searchRecycleViewAdapter
        }
    }

}