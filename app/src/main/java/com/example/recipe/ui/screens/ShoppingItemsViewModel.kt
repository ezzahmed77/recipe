package com.example.recipe.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipe.data.local.model.ShoppingItemLocal
import com.example.recipe.domain.RecipesDefaultRepository
import kotlinx.coroutines.flow.*


sealed interface ShoppingItemsUiState{
    data class Success(val items: List<ShoppingItemLocal>): ShoppingItemsUiState
    object Loading: ShoppingItemsUiState
    data class Error(val message: String): ShoppingItemsUiState
}

class ShoppingItemsViewModel(
    private val defaultRepository: RecipesDefaultRepository
): ViewModel() {

    val uiState: StateFlow<ShoppingItemsUiState> = defaultRepository.getAllShoppingItems().map {
        ShoppingItemsUiState.Success(items = it)
    }.catch {
        ShoppingItemsUiState.Error(message = it.message.toString())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = ShoppingItemsUiState.Loading
    )

    suspend fun deleteShoppingItem(item:ShoppingItemLocal){
        defaultRepository.deleteShoppingItem(item)
    }

}