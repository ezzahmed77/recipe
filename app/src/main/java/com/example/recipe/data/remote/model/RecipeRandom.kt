package com.example.recipe.data.remote.model

import com.squareup.moshi.Json


data class RandomRecipesResponse(
    @field:Json(name = "recipes")
    val recipes: List<RecipeRandom>,

)

data class RecipeRandom(
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "title") val title: String,
    @field:Json(name = "image") val image: String,
    @field:Json(name = "imageType") val imageType: String,
    @field:Json(name = "servings") val servings: Int,
    @field:Json(name = "readyInMinutes") val readyInMinutes: Int,
)