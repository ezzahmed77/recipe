package com.example.recipe.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.recipe.data.remote.model.ExtendedIngredient
import com.google.gson.reflect.TypeToken

@Entity(tableName = "recipes")
@TypeConverters(ExtendedIngredientsConverter::class)
data class RecipeLocalDetail(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val title: String,
    val image: String,
    val imageType: String,
    val servings: Int,
    val readyInMinutes: Int,
    val dairyFree: Boolean,
    val glutenFree: Boolean,
    val vegetarian: Boolean,
    val extendedIngredients: List<ExtendedIngredient>,
    val isFavorite: Boolean = false,
    val isSaved: Boolean = false,
)

class ExtendedIngredientsConverter {
    @TypeConverter
    fun fromExtendedIngredientsList(value: List<ExtendedIngredient>): String {
        val gson = com.google.gson.Gson()
        val type = object : TypeToken<List<ExtendedIngredient>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toExtendedIngredientsList(value: String): List<ExtendedIngredient> {
        val gson = com.google.gson.Gson()
        val type = object : TypeToken<List<ExtendedIngredient>>() {}.type
        return gson.fromJson(value, type)
    }
}
