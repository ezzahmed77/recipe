package com.example.recipe.data.local.recipedata

import androidx.room.*
import com.example.recipe.data.local.model.RecipeLocalDetail
import com.example.recipe.data.local.model.ShoppingItemLocal
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(recipe: RecipeLocalDetail)

    @Update
    suspend fun update(recipe: RecipeLocalDetail)

    @Delete
    suspend fun delete(recipe: RecipeLocalDetail)

    @Query("SELECT * FROM recipes WHERE id = :id")
    fun getRecipe(id: Int): Flow<RecipeLocalDetail>

    @Query("SELECT * FROM recipes ORDER BY title ASC")
    fun getAllRecipes(): Flow<List<RecipeLocalDetail>>

    @Query("SELECT * FROM recipes WHERE isFavorite = 1")
    fun getFavoriteRecipes(): Flow<List<RecipeLocalDetail>>

    @Query("SELECT * FROM recipes WHERE isSaved = 1")
    fun getSavedRecipes(): Flow<List<RecipeLocalDetail>>

    // Methods related to shopping list
    @Insert(onConflict = OnConflictStrategy.NONE)
    suspend fun insertShoppingItem(item: ShoppingItemLocal)

    @Delete
    suspend fun deleteShoppingItem(item: ShoppingItemLocal)

    @Query("SELECT * FROM shopping_list ORDER BY ingredientName ASC")
    fun getAllShoppingItems(): Flow<List<ShoppingItemLocal>>

    @Query("SELECT * FROM shopping_list WHERE recipeId = :recipeId")
    fun getShoppingItemsForRecipe(recipeId: Int): Flow<List<ShoppingItemLocal>>

}
