package com.example.recipe.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipe.data.local.model.RecipeLocalDetail
import com.example.recipe.data.remote.model.RecipeRandom
import com.example.recipe.domain.RecipesDefaultRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed interface RecipesFavoriteUiState{
    data class Success(val recipes: List<RecipeLocalDetail>): RecipesFavoriteUiState
    object Loading: RecipesFavoriteUiState
    data class Error(val message: String): RecipesFavoriteUiState
}


class RecipesFavoriteViewModel(
    private val defaultRepository: RecipesDefaultRepository
): ViewModel() {

    var recipesFavoriteUiState: StateFlow<RecipesFavoriteUiState> = defaultRepository.getFavoriteRecipes().map {
        RecipesFavoriteUiState.Success(recipes = it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = RecipesFavoriteUiState.Loading
    )

    suspend fun removeFromFavorite(recipe: RecipeLocalDetail){
        defaultRepository.updateRecipe(recipe.copy(isFavorite = false))
        if(!recipe.isSaved){
            defaultRepository.deleteRecipe(recipe)
        }
    }

}