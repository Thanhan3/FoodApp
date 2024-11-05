package com.example.foodapp.ui.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.data.Pojo.Category
import com.example.foodapp.data.Pojo.CategoryList
import com.example.foodapp.data.Pojo.MealByCategoryList
import com.example.foodapp.data.Pojo.MealByCategory
import com.example.foodapp.data.Pojo.Meal
import com.example.foodapp.data.Pojo.MealList
import com.example.foodapp.data.local.db.MealDatabase
import com.example.foodapp.data.remote.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    private val mealDatabase: MealDatabase
) : ViewModel() {
    private var _randomMealLiveData: MutableLiveData<Meal> = MutableLiveData()
    private var _popularItemsLiveData: MutableLiveData<List<MealByCategory>> = MutableLiveData()
    private var _categoriesLiveData: MutableLiveData<List<Category>> = MutableLiveData()
    private var _searchMealLiveData: MutableLiveData<List<Meal>> = MutableLiveData()

    val searchMealLiveData: LiveData<List<Meal>>
        get() = _searchMealLiveData
    val categoriesLiveData: LiveData<List<Category>>
        get() = _categoriesLiveData
    val randomMealLiveData: LiveData<Meal>
        get() = _randomMealLiveData
    val popularItemsLiveData: LiveData<List<MealByCategory>>
        get() = _popularItemsLiveData

    var saveRandomMeal :Meal ?= null
    fun getRandomMeal() {
        saveRandomMeal?.let {
            _randomMealLiveData.postValue(it)
            return
        }
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val randomMeal: Meal = response.body()!!.meals[0]
                    _randomMealLiveData.value = randomMeal
                    saveRandomMeal = randomMeal
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("Test", t.message.toString())
            }
        })
    }

    fun getPopularItems() {
        RetrofitInstance.api.getPopularItems("seafood")
            .enqueue(object : Callback<MealByCategoryList> {
                override fun onResponse(
                    call: Call<MealByCategoryList>,
                    response: Response<MealByCategoryList>
                ) {
                    if (response.body() != null) {
                        _popularItemsLiveData.value = response.body()!!.meals
                    } else {
                        return
                    }
                }

                override fun onFailure(call: Call<MealByCategoryList>, t: Throwable) {
                    Log.d("Test", t.message.toString())
                }

            })
    }

    fun getCategories() {
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryList> {
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                if (response.body() != null) {
                    _categoriesLiveData.value = response.body()!!.categories
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("Test", t.message.toString())
            }

        })
    }

    fun searchMeal(name: String) {
        RetrofitInstance.api.searchMeals(name).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val meal = response.body()!!.meals
                    _searchMealLiveData.value = meal
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("Test", t.message.toString())
            }
        })
    }

    fun getAllFavorites(): LiveData<List<Meal>> {
        return mealDatabase.mealDao().getAllMeals()
    }

    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().deleteMeal(meal)
        }
    }

    fun insertMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().insertMeal(meal)
        }
    }
}