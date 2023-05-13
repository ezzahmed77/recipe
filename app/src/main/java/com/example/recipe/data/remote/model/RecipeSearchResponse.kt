package com.example.recipe.data.remote.model

import com.squareup.moshi.Json

data class RecipeSearchResponse(
    @field:Json(name = "results")
    val results: List<RecipeRemoteBasic>,
    @field:Json(name = "offset")
    val offset: Int,
    @field:Json(name = "number")
    val number: Int,
    @field:Json(name = "totalResults")
    val totalResults: Int
)
