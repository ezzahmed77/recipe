package com.example.recipe.domain

import com.example.recipe.data.local.model.RecipeLocalDetail
import com.example.recipe.data.local.model.ShoppingItemLocal
import com.example.recipe.data.local.repositories.RecipeLocalRepository
import com.example.recipe.data.remote.model.RecipeRandom
import com.example.recipe.data.remote.model.RecipeRemoteBasic
import com.example.recipe.data.remote.repositories.RecipeRemoteRepository
import com.example.recipe.utilites.convertors.toRecipeLocalDetail
import kotlinx.coroutines.flow.Flow

interface RecipesDefaultRepository{
    fun getAllRecipes(): Flow<List<RecipeLocalDetail>>

    fun getSavedRecipes(): Flow<List<RecipeLocalDetail>>

    fun getFavoriteRecipes(): Flow<List<RecipeLocalDetail>>

    fun getRecipeById(id: Int): Flow<RecipeLocalDetail>

    suspend fun updateRecipe(recipe: RecipeLocalDetail)

    suspend fun insertRecipe(recipe: RecipeLocalDetail)


    suspend fun deleteRecipe(recipe: RecipeLocalDetail)

    suspend fun getRecipeDetails(id: Int, apiKey: String): RecipeLocalDetail

    suspend fun getRandomRecipes(apiKey: String, number: Int): List<RecipeRandom>

    suspend fun getSearchRecipes(apiKey: String, query: String): List<RecipeRemoteBasic>

    // Methods related to shopping list
    // Methods related to shopping items
    suspend fun insertShoppingItem(item: ShoppingItemLocal)

    suspend fun deleteShoppingItem(item: ShoppingItemLocal)

    fun getAllShoppingItems(): Flow<List<ShoppingItemLocal>>

    fun getShoppingItemsForRecipe(recipeId: Int): Flow<List<ShoppingItemLocal>>



}
class RecipesDefaultRepositoryImpl(
    private val remoteRepository: RecipeRemoteRepository,
    private val localRepository: RecipeLocalRepository
): RecipesDefaultRepository{
    override fun getAllRecipes(): Flow<List<RecipeLocalDetail>> {
        return localRepository.getAllRecipes()
    }

    override fun getSavedRecipes(): Flow<List<RecipeLocalDetail>> {
        return localRepository.getSavedRecipes()
    }

    override fun getFavoriteRecipes(): Flow<List<RecipeLocalDetail>> {
        return localRepository.getFavoriteRecipes()
    }

    override fun getRecipeById(id: Int): Flow<RecipeLocalDetail> {
        return localRepository.getRecipeById(id)
    }

    override suspend fun updateRecipe(recipe: RecipeLocalDetail) {
        return localRepository.updateRecipe(recipe)
    }

    override suspend fun insertRecipe(recipe: RecipeLocalDetail) {
        return localRepository.insertRecipe(recipe)
    }

    override suspend fun deleteRecipe(recipe: RecipeLocalDetail) {
        return localRepository.deleteRecipe(recipe)
    }

    override suspend fun getRecipeDetails(id: Int, apiKey: String): RecipeLocalDetail {
        return remoteRepository.getRecipeDetails(id = id, apiKey = apiKey).toRecipeLocalDetail()
    }

    override suspend fun getRandomRecipes(apiKey: String, number: Int): List<RecipeRandom> {
        return remoteRepository.getRandomRecipes(apiKey, number)
    }

    override suspend fun getSearchRecipes(apiKey: String, query: String): List<RecipeRemoteBasic> {
        return remoteRepository.getSearchRecipes(apiKey = apiKey, query = query)
    }

    override suspend fun insertShoppingItem(item: ShoppingItemLocal) {
        return localRepository.insertShoppingItem(item)
    }

    override suspend fun deleteShoppingItem(item: ShoppingItemLocal) {
        return localRepository.deleteShoppingItem(item)
    }

    override fun getAllShoppingItems(): Flow<List<ShoppingItemLocal>> {
        return localRepository.getAllShoppingItems()
    }

    override fun getShoppingItemsForRecipe(recipeId: Int): Flow<List<ShoppingItemLocal>> {
        return localRepository.getShoppingItemsForRecipe(recipeId = recipeId)
    }


}
