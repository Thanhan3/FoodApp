package com.example.foodapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodapp.R
import com.example.foodapp.data.Pojo.MealByCategory
import com.example.foodapp.data.Pojo.Meal
import com.example.foodapp.data.local.db.MealDatabase
import com.example.foodapp.databinding.FragmentHomeBinding
import com.example.foodapp.ui.home.adapter.CategoryAdapter
import com.example.foodapp.ui.home.adapter.MostPopularAdapter
import com.example.foodapp.ui.home.viewmodel.HomeViewModel
import com.example.foodapp.ui.home.viewmodel.HomeViewModelFactory


class HomeFragment : Fragment() {
    private lateinit var homeBinding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by activityViewModels(
        factoryProducer = {
            val mealDatabase = MealDatabase.getInstance(requireContext())
            HomeViewModelFactory(mealDatabase)
        }
    )
    private lateinit var randomMeal: Meal
    private lateinit var popularItemsAdapter: MostPopularAdapter
    private lateinit var categoryAdapter: CategoryAdapter

    companion object{
        const val MEAL_ID = "com.example.foodapp.ui.home.idMeal"
        const val MEAL_NAME = "com.example.foodapp.ui.home.nameMeal"
        const val MEAL_THUMB = "com.example.foodapp.ui.home.thumbMeal"
        const val CATEGORY_NAME = "com.example.foodapp.ui.home.categoryName"

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        preparePopularItemRecyclerView()
        prepareCategoryRecyclerView()
        setupViewModel()
        observeData()
        onRandomMealClick()
        onPopularItemClick()
        onCategoryClick()
        onSearchedMealClick()
    }

    private fun prepareCategoryRecyclerView() {
        homeBinding.rvCategory.apply {
            layoutManager = GridLayoutManager(activity,3, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
        }
    }

    private fun setupView() {
        popularItemsAdapter = MostPopularAdapter()
        categoryAdapter = CategoryAdapter()
    }

    private fun preparePopularItemRecyclerView() {
        homeBinding.rvMealPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemsAdapter
        }

    }


    private fun setupViewModel() {
        homeViewModel.getRandomMeal()
        homeViewModel.getPopularItems()
        homeViewModel.getCategories()
    }

    private fun observeData() {
        homeViewModel.randomMealLiveData.observe(viewLifecycleOwner) { meal ->
            Glide.with(this@HomeFragment)
                .load(meal.strMealThumb)
                .into(homeBinding.imgRandomMeal)
            this.randomMeal = meal
        }
        homeViewModel.popularItemsLiveData.observe(viewLifecycleOwner) { mealsList ->
            popularItemsAdapter.setMeal(mealsList as ArrayList<MealByCategory>)
        }
        homeViewModel.categoriesLiveData.observe(viewLifecycleOwner){categories->
            categoryAdapter.setCategoryList(categories)

        }
    }
    private fun onSearchedMealClick() {
        homeBinding.imgSearch.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
    }

    private fun onRandomMealClick() {
        homeBinding.randomMealCard.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID,randomMeal.idMeal)
            intent.putExtra(MEAL_NAME,randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB,randomMeal.strMealThumb)
            startActivity(intent)
        }
    }
    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID,meal.idMeal)
            intent.putExtra(MEAL_NAME,meal.strMeal)
            intent.putExtra(MEAL_THUMB,meal.strMealThumb)
            startActivity(intent)
        }
    }
    private fun onCategoryClick(){
        categoryAdapter.onItemClick = { category ->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME,category.strCategory)
            startActivity(intent)
        }
    }


}