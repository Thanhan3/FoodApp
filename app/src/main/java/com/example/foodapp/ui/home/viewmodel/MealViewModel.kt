package com.example.foodapp.ui.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.data.Pojo.Meal
import com.example.foodapp.data.Pojo.MealList
import com.example.foodapp.data.local.db.MealDatabase
import com.example.foodapp.data.remote.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel(
    val mealDatabase: MealDatabase
) : ViewModel() {
    private var _mealDetail: MutableLiveData<Meal> = MutableLiveData()

    val mealDetail: LiveData<Meal>
        get() = _mealDetail

    fun getMealDetail(mealId: String) {
        RetrofitInstance.api.getMealDetails(mealId).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    _mealDetail.value = response.body()!!.meals[0]
                } else {
                    return
                }
            }

            override fun onFailure(p0: Call<MealList>, t: Throwable) {
                Log.d("Test", t.message.toString())
            }
        })
    }
    fun insertMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().insertMeal(meal)
        }
    }


}

