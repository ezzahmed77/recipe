package com.example.recipe.data.remote.model

import com.squareup.moshi.Json

data class RecipeRemoteBasic(
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "title")
    val title: String,
    @field:Json(name = "image")
    val image: String = "",
    @field:Json(name = "imageType")
    val imageType: String = ""
)
