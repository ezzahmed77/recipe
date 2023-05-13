package com.example.recipe.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipe.data.local.model.RecipeLocalDetail
import com.example.recipe.domain.RecipesDefaultRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed interface RecipesSavedUiState{
    data class Success(val recipes: List<RecipeLocalDetail>): RecipesSavedUiState
    object Loading: RecipesSavedUiState
    data class Error(val message: String): RecipesSavedUiState
}

class RecipesSavedViewModel(
    private val defaultRepository: RecipesDefaultRepository
): ViewModel() {

    var recipesSavedUiState: StateFlow<RecipesSavedUiState> = defaultRepository.getSavedRecipes().map {
        RecipesSavedUiState.Success(recipes = it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = RecipesSavedUiState.Loading
    )

    suspend fun removeFromSaved(recipe: RecipeLocalDetail){
        defaultRepository.updateRecipe(recipe.copy(isSaved = false))
        if(!recipe.isFavorite){
            defaultRepository.deleteRecipe(recipe)
        }
    }
}