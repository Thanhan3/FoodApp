package com.example.foodapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.foodapp.R
import com.example.foodapp.data.local.db.MealDatabase
import com.example.foodapp.ui.home.viewmodel.HomeViewModel
import com.example.foodapp.ui.home.viewmodel.HomeViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.properties.ReadOnlyProperty

class MainActivity : AppCompatActivity() {
    val viewModel: HomeViewModel by lazy {
        val mealDatabase = MealDatabase.getInstance(this)
        val homeViewModelFactory = HomeViewModelFactory(mealDatabase)
        ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Lấy NavController từ NavHostFragment thay vì sử dụng findNavController
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.host_fragment) as? NavHostFragment
        val navController = navHostFragment?.navController

        // Kiểm tra và liên kết NavController với BottomNavigationView
        if (navController != null) {
            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.btm_nav)
            bottomNavigationView.setupWithNavController(navController)
        } else {
            throw IllegalStateException("NavController not found on FragmentContainerView")
        }

    }
}