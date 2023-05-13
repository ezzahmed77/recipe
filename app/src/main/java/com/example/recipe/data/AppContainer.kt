package com.example.recipe.data

import android.content.Context
import com.example.recipe.data.local.recipedata.RecipeDatabase
import com.example.recipe.data.local.repositories.RecipeLocalRepository
import com.example.recipe.data.local.repositories.RecipeLocalRepositoryImpl
import com.example.recipe.data.remote.recipeservice.RecipeAPIService
import com.example.recipe.data.remote.repositories.RecipeRemoteRepository
import com.example.recipe.data.remote.repositories.RecipeRemoteRepositoryImpl
import com.example.recipe.domain.RecipesDefaultRepository
import com.example.recipe.domain.RecipesDefaultRepositoryImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

interface AppContainer {
    val remoteRepository: RecipeRemoteRepository
    val localRepository: RecipeLocalRepository
    val defaultRepository: RecipesDefaultRepository
}

class DefaultAppContainer(context: Context)
 : AppContainer {

    // The URL
    private val BASE_URL = "https://api.spoonacular.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .callTimeout(30, TimeUnit.SECONDS) // set the timeout value to 30 seconds
        .build()

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private val retrofitService: RecipeAPIService by lazy {
            retrofit.create(RecipeAPIService::class.java)
    }



    override val remoteRepository: RecipeRemoteRepository by lazy {
        RecipeRemoteRepositoryImpl(retrofitService = retrofitService)
    }
    override val localRepository: RecipeLocalRepository by lazy {
        RecipeLocalRepositoryImpl(RecipeDatabase.getDatabase(context).recipeDao())
    }
    override val defaultRepository: RecipesDefaultRepository by lazy {
        RecipesDefaultRepositoryImpl(remoteRepository = remoteRepository, localRepository = localRepository)
    }

}