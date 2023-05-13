package com.example.recipe.data.local.repositories

import com.example.recipe.data.local.model.RecipeLocalDetail
import com.example.recipe.data.local.model.ShoppingItemLocal
import com.example.recipe.data.local.recipedata.RecipeDao
import kotlinx.coroutines.flow.Flow

class RecipeLocalRepositoryImpl(private val recipeDao: RecipeDao): RecipeLocalRepository {
    override suspend fun insertRecipe(recipe: RecipeLocalDetail) {
        return recipeDao.insert(recipe)
    }

    override suspend fun updateRecipe(recipe: RecipeLocalDetail) {
        return recipeDao.update(recipe)
    }

    override suspend fun deleteRecipe(recipe: RecipeLocalDetail) {
        return recipeDao.delete(recipe)
    }

    override fun getRecipeById(id: Int): Flow<RecipeLocalDetail> {
        return recipeDao.getRecipe(id)
    }

    override fun getAllRecipes(): Flow<List<RecipeLocalDetail>> {
        return recipeDao.getAllRecipes()
    }

    override fun getFavoriteRecipes(): Flow<List<RecipeLocalDetail>> {
        return recipeDao.getFavoriteRecipes()
    }

    override fun getSavedRecipes(): Flow<List<RecipeLocalDetail>> {
        return recipeDao.getSavedRecipes()
    }

    override suspend fun insertShoppingItem(item: ShoppingItemLocal) {
        return recipeDao.insertShoppingItem(item)
    }

    override suspend fun deleteShoppingItem(item: ShoppingItemLocal) {
        return recipeDao.deleteShoppingItem(item)
    }

    override fun getAllShoppingItems(): Flow<List<ShoppingItemLocal>> {
        return recipeDao.getAllShoppingItems()
    }

    override fun getShoppingItemsForRecipe(recipeId: Int): Flow<List<ShoppingItemLocal>> {
        return recipeDao.getShoppingItemsForRecipe(recipeId = recipeId)
    }

}