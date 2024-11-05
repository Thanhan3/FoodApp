package com.example.foodapp.ui.favorite

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.data.local.db.MealDatabase
import com.example.foodapp.databinding.FragmentFavoriteBinding
import com.example.foodapp.ui.favorite.adapter.MealAdapter
import com.example.foodapp.ui.home.HomeFragment.Companion.MEAL_ID
import com.example.foodapp.ui.home.HomeFragment.Companion.MEAL_NAME
import com.example.foodapp.ui.home.HomeFragment.Companion.MEAL_THUMB
import com.example.foodapp.ui.home.MealActivity
import com.example.foodapp.ui.home.viewmodel.HomeViewModel
import com.example.foodapp.ui.home.viewmodel.HomeViewModelFactory
import com.google.android.material.snackbar.Snackbar


class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private val mMealAdapter = MealAdapter()
    private val homeViewModel: HomeViewModel by activityViewModels(
        factoryProducer = {
            val mealDatabase = MealDatabase.getInstance(requireContext())
            HomeViewModelFactory(mealDatabase)
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecyclerView()
        observeData()
        onItemClick()
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedMeal = mMealAdapter.differ.currentList[position]
                val meal = mMealAdapter.differ.currentList[position]
                homeViewModel.deleteMeal(deletedMeal)
                Snackbar.make(requireView(), "Meal deleted", Snackbar.LENGTH_LONG).setAction(
                    "Undo",
                    View.OnClickListener {
                        homeViewModel.insertMeal(meal)
                    }
                ).show()
            }
        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavMeals)
    }

    private fun onItemClick() {
        mMealAdapter.onItemClick = { favoriteMeal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, favoriteMeal.idMeal)
            intent.putExtra(MEAL_NAME, favoriteMeal.strMeal)
            intent.putExtra(MEAL_THUMB, favoriteMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun prepareRecyclerView() {
        binding.rvFavMeals.apply {
            layoutManager = GridLayoutManager(activity, 2, LinearLayoutManager.VERTICAL, false)
            adapter = mMealAdapter
        }
    }

    private fun observeData() {
        homeViewModel.getAllFavorites().observe(viewLifecycleOwner) { meals ->
            mMealAdapter.differ.submitList(meals)
        }
    }
}