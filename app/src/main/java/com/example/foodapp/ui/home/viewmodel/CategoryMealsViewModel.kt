package com.example.foodapp.ui.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodapp.data.Pojo.MealByCategory
import com.example.foodapp.data.Pojo.MealByCategoryList
import com.example.foodapp.data.remote.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMealsViewModel : ViewModel() {
    private val _mealsLiveData: MutableLiveData<List<MealByCategory>> = MutableLiveData()
    val mealsLiveData: LiveData<List<MealByCategory>>
        get() = _mealsLiveData

    fun getMealByCategory(categoryName: String){
        RetrofitInstance.api.getPopularItems(categoryName).enqueue(object :
            Callback<MealByCategoryList> {
            override fun onResponse(call: Call<MealByCategoryList>, response: Response<MealByCategoryList>) {
                if (response.body() != null) {
                    _mealsLiveData.value =response.body()!!.meals
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<MealByCategoryList>, t: Throwable) {
                Log.d("Test", t.message.toString())
            }

        })
    }
}