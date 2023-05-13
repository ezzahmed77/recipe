package com.example.recipe.data.remote.recipeservice

import com.example.recipe.data.remote.model.RandomRecipesResponse
import com.example.recipe.data.remote.model.RecipeRemoteDetail
import com.example.recipe.data.remote.model.RecipeSearchResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface RecipeAPIService {
    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("query") query: String,
        @Query("apiKey") apiKey: String
    ): Response<RecipeSearchResponse>

    @GET("recipes/{id}/information")
    suspend fun getRecipeDetails(@Path("id") id: Int, @Query("apiKey") apiKey: String): Response<RecipeRemoteDetail>

    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("apiKey") apiKey: String,
        @Query("number") number: Int,
    ): Response<RandomRecipesResponse>

}


