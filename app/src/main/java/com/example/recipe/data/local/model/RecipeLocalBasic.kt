package com.example.recipe.data.local.model

data class RecipeLocalBasic(
    val id: Int,
    val title: String,
    val image: String,
    val imageType: String,
    val isFavorite: Boolean = false,
    val isSaved: Boolean = false,
)
