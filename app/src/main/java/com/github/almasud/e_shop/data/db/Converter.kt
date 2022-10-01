package com.github.almasud.e_shop.data.db

import androidx.room.TypeConverter
import com.github.almasud.e_shop.domain.model.Parent
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class Converter {

    @TypeConverter
    fun fromParentListToJSON(parents: List<Parent>): String {
        return Gson().toJson(parents)
    }

    @TypeConverter
    fun fromJSONToParentList(json: String): List<Parent> {
        val type: Type = object : TypeToken<List<Parent>>() {}.type
        return Gson().fromJson(json, type)
    }
}