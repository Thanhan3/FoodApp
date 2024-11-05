package com.example.foodapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.data.Pojo.Category
import com.example.foodapp.data.Pojo.MealByCategory
import com.example.foodapp.databinding.MealItemBinding

class CategoryMealsAdapter() :
    RecyclerView.Adapter<CategoryMealsAdapter.CategoryMealsViewHolder>() {
    var onItemClick: ((MealByCategory) -> Unit)? = null
    private val mealList = ArrayList<MealByCategory>()
    fun setMealList(mealList: List<MealByCategory>) {
        this.mealList.clear()
        this.mealList.addAll(mealList)
        notifyDataSetChanged()
    }

    class CategoryMealsViewHolder(var binding: MealItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealsViewHolder {
        return CategoryMealsViewHolder(
            MealItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    override fun onBindViewHolder(holder: CategoryMealsViewHolder, position: Int) {
        holder.binding.tvMealName.text = mealList[position].strMeal
        Glide.with(holder.itemView)
            .load(mealList[position].strMealThumb)
            .into(holder.binding.imgMeal)
        holder.itemView.setOnClickListener {
            onItemClick!!.invoke(mealList[position])

        }
    }
}