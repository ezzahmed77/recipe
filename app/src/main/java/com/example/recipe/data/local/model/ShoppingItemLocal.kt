package com.example.recipe.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_list")
data class ShoppingItemLocal(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val recipeId: Int,
    val ingredientName: String,
    val ingredientAmount: Double,
    val ingredientUnit: String
)
