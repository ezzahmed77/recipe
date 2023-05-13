package com.example.recipe

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.recipe.ui.screens.*

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(
                recipeApplication().container.defaultRepository
            )
        }

        // Initializer for RecipesSavedViewModel
        initializer {
            RecipesSavedViewModel(
                recipeApplication().container.defaultRepository
            )
        }

        // Initializer for RecipesFavoriteViewModel
        initializer {
            RecipesFavoriteViewModel(
                recipeApplication().container.defaultRepository
            )
        }

        // Initializer for RecipeDetailViewModel
        initializer {
            RecipeDetailViewModel(
                this.createSavedStateHandle(),
                recipeApplication().container.defaultRepository
            )
        }

        // Initializer for ShoppingItemsViewModel
        initializer {
            ShoppingItemsViewModel(
                recipeApplication().container.defaultRepository
            )
        }

        // Initializer for LogInViewModel
        initializer {
            LoginViewModel()
        }
    }
}

fun CreationExtras.recipeApplication(): RecipeApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RecipeApplication)

