package com.example.foodapp.data.local.db

import androidx.room.TypeConverter
import androidx.room.TypeConverters

@TypeConverters
class MealTypeConvertor {

    @TypeConverter
    fun formAnyToString(any: Any?): String {
        return if(any == null) ""
        else any as String
    }

    @TypeConverter
    fun formStringToAny(string: String?): Any {
        return string ?: ""
    }
}