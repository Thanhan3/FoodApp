package com.example.foodapp.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodapp.R
import com.example.foodapp.data.Pojo.Meal
import com.example.foodapp.data.local.db.MealDatabase
import com.example.foodapp.databinding.ActivityMealBinding
import com.example.foodapp.databinding.FragmentHomeBinding
import com.example.foodapp.ui.home.viewmodel.MealViewModel
import com.example.foodapp.ui.home.viewmodel.MealViewModelFactory

class MealActivity : AppCompatActivity() {
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var mealMvvm: MealViewModel
    private lateinit var binding: ActivityMealBinding
    private lateinit var mealDetail: Meal
    private var mealToSave: Meal? = null
    private lateinit var mealDatabase: MealDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        getMealInformation()
        setupView()
        observeMealDetailLiveData()
        onYoutubeImageClick()
        onFavoriteClick()
    }

    private fun onFavoriteClick() {
        binding.btnAddToFav.setOnClickListener {
            mealToSave?.let {
                mealMvvm.insertMeal(it)
                Toast.makeText(this, "Meal saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onYoutubeImageClick() {
        binding.imgYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mealDetail.strYoutube))
            startActivity(intent)
        }
    }

    private fun getMealInformation() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }

    private fun setupView() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)
        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
//        mealMvvm = ViewModelProvider(this)[MealViewModel::class.java]
        mealDatabase = MealDatabase.getInstance(this)
        val factory = MealViewModelFactory(mealDatabase)
        mealMvvm = ViewModelProvider(this, factory)[MealViewModel::class.java]
        mealMvvm.getMealDetail(mealId)
    }

    @SuppressLint("SetTextI18n")
    private fun observeMealDetailLiveData() {
        mealMvvm.mealDetail.observe(this, Observer {
            mealDetail = it
            mealToSave = it
            binding.mealArea.text = "Area: ${mealDetail.strArea}"
            binding.mealCategory.text = "Category: ${mealDetail.strCategory}"
            binding.mealInstruction.text = mealDetail.strInstructions
        })
    }

}