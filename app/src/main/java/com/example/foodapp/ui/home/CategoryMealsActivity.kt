package com.example.foodapp.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.get
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodapp.R
import com.example.foodapp.databinding.ActivityCategoryMealsBinding
import com.example.foodapp.ui.home.HomeFragment.Companion.MEAL_ID
import com.example.foodapp.ui.home.HomeFragment.Companion.MEAL_NAME
import com.example.foodapp.ui.home.HomeFragment.Companion.MEAL_THUMB
import com.example.foodapp.ui.home.adapter.CategoryAdapter
import com.example.foodapp.ui.home.adapter.CategoryMealsAdapter
import com.example.foodapp.ui.home.viewmodel.CategoryMealsViewModel

class CategoryMealsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryMealsBinding
    private lateinit var categoryMealsViewModel: CategoryMealsViewModel
    private lateinit var categoryMealsAdapter : CategoryMealsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        prepareRecyclerView()
        setupViewModel()
        obserMealLiveData()
        onItemClick()
    }

    private fun prepareRecyclerView() {
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.rvMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }
        binding.rvMeals.adapter = categoryMealsAdapter

    }

    private fun obserMealLiveData() {
        categoryMealsViewModel.getMealByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)
        categoryMealsViewModel.mealsLiveData.observe(this) { mealsList ->
            binding.tvCategoryCount.text = mealsList.size.toString()
            categoryMealsAdapter.setMealList(mealsList)
        }
    }

    private fun setupViewModel() {
        categoryMealsViewModel = ViewModelProvider(this)[CategoryMealsViewModel::class.java]
    }
    private fun onItemClick() {
        categoryMealsAdapter.onItemClick ={ meal ->
            val intent = Intent(applicationContext, MealActivity::class.java)
            intent.putExtra(MEAL_ID,meal.idMeal)
            intent.putExtra(MEAL_NAME,meal.strMeal)
            intent.putExtra(MEAL_THUMB,meal.strMealThumb)
            startActivity(intent)

        }
    }

}