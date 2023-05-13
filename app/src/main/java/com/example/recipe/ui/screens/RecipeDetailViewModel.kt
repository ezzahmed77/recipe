package com.example.recipe.ui.screens

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipe.data.local.model.RecipeLocalDetail
import com.example.recipe.data.local.model.ShoppingItemLocal
import com.example.recipe.data.remote.model.ExtendedIngredient
import com.example.recipe.domain.RecipesDefaultRepository
import com.example.recipe.utilites.convertors.getEmptyRecipeLocalDetail
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed interface RecipeDetailUiState{
    data class Success(val recipe: RecipeLocalDetail): RecipeDetailUiState
    object Loading: RecipeDetailUiState
    data class Error(val message: String): RecipeDetailUiState
}


class RecipeDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val defaultRepository: RecipesDefaultRepository
    ): ViewModel(){

    private val recipeId: Int = checkNotNull(savedStateHandle[RecipeDetailsDestination.itemIdArg])
    val uiState: MutableStateFlow<RecipeDetailUiState> = MutableStateFlow(RecipeDetailUiState.Loading)
    var recipeUiState : MutableStateFlow<RecipeLocalDetail> = MutableStateFlow(getEmptyRecipeLocalDetail())

     val addedShoppingIngredientsState: StateFlow<List<ShoppingItemLocal>> =
        defaultRepository.getAllShoppingItems()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = emptyList()
            )

    val similarIngredients: StateFlow<List<ExtendedIngredient>> = combine(
        recipeUiState,
        addedShoppingIngredientsState
    ) { recipeUiStateValue, addedShoppingIngredientsStateValue ->
        recipeUiStateValue.extendedIngredients.filter { extendedIngredient ->
            addedShoppingIngredientsStateValue.filter{
                it.recipeId == recipeUiState.value.id
            }.any { shoppingItem ->
                extendedIngredient.name == shoppingItem.ingredientName &&
                        extendedIngredient.amount == shoppingItem.ingredientAmount &&
                        extendedIngredient.unit == shoppingItem.ingredientUnit
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )



    init {
        getRecipeDetails()
    }

    private fun getRecipeDetails(){

        viewModelScope.launch {
            try {
                // first try to get the recipe from DB
                val recipeDetailsLocal = async {
                    defaultRepository.getRecipeById(recipeId).first()
                }.await()

                if(recipeDetailsLocal == null){
                    // Doesn't exist in DB
                    // then fetch recipe details from the API
                    val recipeDetailsRemote = defaultRepository.getRecipeDetails(recipeId, api_key)
                    uiState.value = RecipeDetailUiState.Success(recipe = recipeDetailsRemote)
                    recipeUiState.value = recipeDetailsRemote
                    Log.i("TAG", "id for ui is ${recipeUiState.value.id} --> for Recipe Remote ${recipeDetailsRemote.id}")
                }else{
                    uiState.value = RecipeDetailUiState.Success(recipe = recipeDetailsLocal)
                    recipeUiState.value = recipeDetailsLocal
                    Log.i("TAG", "id for ui is ${recipeUiState.value.id} --> for Recipe Local ${recipeDetailsLocal.id}")

                }

            } catch (e: Exception) {
                uiState.value = RecipeDetailUiState.Error(message = e.message.toString())
            }
        }
    }

    // Saving and Removing
    suspend fun saveRecipe(){
        // Update recipe
        recipeUiState.value = recipeUiState.value.copy(isSaved = true)
        saveRecipeToLocalDB(recipe = recipeUiState.value)

    }

    suspend fun removeRecipe(){
        // Update recipe
        recipeUiState.value = recipeUiState.value.copy(isSaved = false)
        defaultRepository.updateRecipe(recipe = recipeUiState.value)
        updateRecipeFavoriteAndSaved()
    }

    // Adding and removing from Favorites
    suspend fun addRecipeToFavorites(){
        recipeUiState.value = recipeUiState.value.copy(isFavorite = true)
        saveRecipeToLocalDB(recipe = recipeUiState.value)
        defaultRepository.updateRecipe(recipe = recipeUiState.value)
    }

    suspend fun removeRecipeFromFavorites(){
        recipeUiState.value = recipeUiState.value.copy(isFavorite = false)
        defaultRepository.updateRecipe(recipe = recipeUiState.value)
        updateRecipeFavoriteAndSaved()

    }

    private suspend fun saveRecipeToLocalDB(recipe: RecipeLocalDetail){
        defaultRepository.insertRecipe(recipe = recipe)
    }

    private suspend fun updateRecipeFavoriteAndSaved(){
        if(!recipeUiState.value.isSaved && !recipeUiState.value.isFavorite) {
            defaultRepository.deleteRecipe(recipe = recipeUiState.value)
        }
    }

    // Methods related to shopping items
    suspend fun addIngredientToShoppingItems(ingredient: ExtendedIngredient) {
        val shoppingItemLocal = ShoppingItemLocal(
            recipeId = recipeUiState.value.id,
            ingredientName = ingredient.name,
            ingredientAmount = ingredient.amount,
            ingredientUnit = ingredient.unit
        )
        defaultRepository.insertShoppingItem(shoppingItemLocal)
    }

    suspend fun removeIngredientFromShoppingItem(ingredient: ExtendedIngredient) {
        val shoppingItem: ShoppingItemLocal = defaultRepository.getShoppingItemsForRecipe(recipeId = recipeUiState.value.id)
            .first()
            .filter {
                it.ingredientName == ingredient.name
                        && it.ingredientAmount == ingredient.amount
                        && it.ingredientUnit == ingredient.unit
            }.first()
        defaultRepository.deleteShoppingItem(shoppingItem)
    }

}