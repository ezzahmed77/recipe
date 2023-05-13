package com.example.recipe.data.remote.model

import com.squareup.moshi.Json

data class ExtendedIngredient(
    @field:Json(name = "aisle") val aisle: String,
    @field:Json(name = "amount") val amount: Double,
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "image") val image: String,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "original") val original: String,
    @field:Json(name = "unit") val unit: String
)


data class RecipeRemoteDetail(
    @field:Json(name = "id")
    val id: Int,

    @field:Json(name = "title")
    val title: String,

    @field:Json(name = "image")
    val image: String,

    @field:Json(name = "imageType")
    val imageType: String,

    @field:Json(name = "servings")
    val servings: Int,

    @field:Json(name = "readyInMinutes")
    val readyInMinutes: Int,

    @field:Json(name = "dairyFree")
    val dairyFree: Boolean,

    @field:Json(name = "glutenFree")
    val glutenFree: Boolean,

    @field:Json(name = "vegetarian")
    val vegetarian: Boolean,

    @field:Json(name = "extendedIngredients")
    val extendedIngredients: List<ExtendedIngredient>
)
