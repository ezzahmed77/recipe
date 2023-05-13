package com.example.recipe.ui.screens

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import com.example.recipe.AppViewModelProvider
import com.example.recipe.R
import com.example.recipe.data.local.model.RecipeLocalDetail
import com.example.recipe.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch


object RecipesSavedDestination : NavigationDestination {
    override val route = "saved_recipes"
    override val titleRes = R.string.saved_recipes
}
@Composable
fun RecipesSavedScreen(
    navigateUp: () -> Unit,
    navigateToRecipeDetail: (Int) -> Unit,
    viewModel: RecipesSavedViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = AppViewModelProvider.Factory)
)
{
    val uiState = viewModel.recipesSavedUiState.collectAsState().value
    val scope = rememberCoroutineScope()
    if(uiState is RecipesSavedUiState.Success){
        LazyVerticalGrid(GridCells.Fixed(2)) {
            items(uiState.recipes.size) { index ->
                RecipeCard(
                    recipe = uiState.recipes[index],
                    onIconClick = { scope.launch { viewModel.removeFromSaved(recipe = uiState.recipes[index]) }  },
                    icon = Icons.Default.ThumbUp,
                    onRecipeClick = {navigateToRecipeDetail(it.id)},
                    tint = if (uiState.recipes[index].isSaved) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground,
                )
            }
        }
    }else{
        // Show Loading Screen
    }

}