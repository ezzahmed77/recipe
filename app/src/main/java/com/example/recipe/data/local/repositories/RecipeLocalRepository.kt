package com.example.recipe.data.local.repositories

import com.example.recipe.data.local.model.RecipeLocalDetail
import com.example.recipe.data.local.model.ShoppingItemLocal
import kotlinx.coroutines.flow.Flow

interface RecipeLocalRepository{
    suspend fun insertRecipe(recipe: RecipeLocalDetail)

    suspend fun updateRecipe(recipe: RecipeLocalDetail)

    suspend fun deleteRecipe(recipe: RecipeLocalDetail)

    fun getRecipeById(id: Int): Flow<RecipeLocalDetail>

    fun getAllRecipes(): Flow<List<RecipeLocalDetail>>

    fun getFavoriteRecipes(): Flow<List<RecipeLocalDetail>>

    fun getSavedRecipes(): Flow<List<RecipeLocalDetail>>

    // Methods related to shopping items
    suspend fun insertShoppingItem(item: ShoppingItemLocal)

    suspend fun deleteShoppingItem(item: ShoppingItemLocal)

    fun getAllShoppingItems(): Flow<List<ShoppingItemLocal>>

    fun getShoppingItemsForRecipe(recipeId: Int): Flow<List<ShoppingItemLocal>>
}
