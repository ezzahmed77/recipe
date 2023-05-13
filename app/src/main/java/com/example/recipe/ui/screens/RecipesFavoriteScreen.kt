package com.example.recipe.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.recipe.AppViewModelProvider
import com.example.recipe.R
import com.example.recipe.data.local.model.RecipeLocalDetail
import com.example.recipe.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch


object RecipesFavoriteDestination : NavigationDestination {
    override val route = "favorites"
    override val titleRes = R.string.favorite_recipes
}

@Composable
fun RecipesFavoriteScreen(
    navigateUp: () -> Unit,
    navigateToRecipeDetail: (Int) -> Unit,
    viewModel: RecipesFavoriteViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = AppViewModelProvider.Factory)
) {
    val scope = rememberCoroutineScope()
    val uiState = viewModel.recipesFavoriteUiState.collectAsState().value
    if(uiState is RecipesFavoriteUiState.Success){
        LazyVerticalGrid(GridCells.Fixed(2)) {
            items(uiState.recipes.size) { index ->
                RecipeCard(
                    recipe = uiState.recipes[index],
                    onIconClick = { scope.launch { viewModel.removeFromFavorite(recipe = uiState.recipes[index]) }},
                    onRecipeClick = {navigateToRecipeDetail(it.id)},
                    icon = Icons.Default.Favorite,
                    tint = if (uiState.recipes[index].isFavorite) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground,
                )
            }
        }
    }
}

@Composable
fun RecipeCard(
    recipe: RecipeLocalDetail,
    onIconClick: (RecipeLocalDetail) -> Unit,
    onRecipeClick: (RecipeLocalDetail) -> Unit,
    icon: ImageVector,
    tint: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onRecipeClick(recipe) },
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Image(
                painter = rememberImagePainter(recipe.image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(shape = RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Text(
                text = recipe.title,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onIconClick(recipe) }
                )
                Text(
                    text = "${recipe.servings} servings | ${recipe.readyInMinutes} mins",
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}
