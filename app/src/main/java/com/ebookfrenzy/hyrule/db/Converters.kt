package com.ebookfrenzy.hyrule.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun commonLocToString(stringList: List<String>?): String {
        if (stringList.isNullOrEmpty()) {
            return ""
        }
        return Gson().toJson(stringList)
    }

    @TypeConverter
    fun stringLocToList(loc: String?): List<String> {
        if (loc.isNullOrBlank()) {
            return listOf("")
        }
        val typeToken = object: TypeToken<List<String>>() {}.type
        return Gson().fromJson(loc, typeToken)
    }
}