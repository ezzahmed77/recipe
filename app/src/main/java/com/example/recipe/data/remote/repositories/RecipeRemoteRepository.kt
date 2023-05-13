package com.example.recipe.data.remote.repositories

import com.example.recipe.data.remote.recipeservice.RecipeAPIService
import com.example.recipe.data.remote.model.RecipeRandom
import com.example.recipe.data.remote.model.RecipeRemoteBasic
import com.example.recipe.data.remote.model.RecipeRemoteDetail

interface RecipeRemoteRepository {
    suspend fun getSearchRecipes(apiKey: String, query: String): List<RecipeRemoteBasic>

    suspend fun getRecipeDetails(apiKey: String, id: Int): RecipeRemoteDetail

    suspend fun getRandomRecipes(apiKey: String, number: Int): List<RecipeRandom>
}

class RecipeRemoteRepositoryImpl(
    private val retrofitService: RecipeAPIService
    ): RecipeRemoteRepository{
    override suspend fun getSearchRecipes(apiKey: String, query: String): List<RecipeRemoteBasic> {
        return retrofitService.searchRecipes(query = query, apiKey = apiKey).body()?.results ?: emptyList()
    }

    override suspend fun getRecipeDetails(apiKey: String, id: Int): RecipeRemoteDetail {
        return retrofitService.getRecipeDetails(id = id, apiKey = apiKey).body()!!
    }

    override suspend fun getRandomRecipes(apiKey: String, number: Int): List<RecipeRandom> {
        return retrofitService.getRandomRecipes(apiKey, number).body()?.recipes ?: emptyList()
    }

}