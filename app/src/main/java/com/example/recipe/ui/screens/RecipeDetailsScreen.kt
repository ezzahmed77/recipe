package com.example.recipe.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.recipe.AppViewModelProvider
import com.example.recipe.R
import com.example.recipe.data.remote.model.ExtendedIngredient
import com.example.recipe.ui.navigation.NavigationDestination
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

object RecipeDetailsDestination : NavigationDestination {
    override val route = "recipe_details"
    override val titleRes = R.string.recipe_detail
    const val itemIdArg = "recipeId"
    val routeWithArgs = "$route/{$itemIdArg}"
}


@Composable
fun RecipeDetailsScreen(
    navigateUp: () -> Unit,
    viewModel: RecipeDetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.uiState.collectAsState().value
    val recipeUiState = viewModel.recipeUiState.collectAsState().value
    val scope = rememberCoroutineScope()
    // For Ingredient in shopping items list
    val similarIngredientsList by viewModel.similarIngredients.collectAsState()

    //todo
    val addedShoppingItemsIngredient by viewModel.addedShoppingIngredientsState.collectAsState()
    if(uiState is RecipeDetailUiState.Success){

        val onFavoriteClick = {
            if(recipeUiState.isFavorite){
                scope.launch { viewModel.removeRecipeFromFavorites() }
            }else{
                scope.launch { viewModel.addRecipeToFavorites() }
            }
        }

        val onSavedClick = {
            if(recipeUiState.isSaved){
                scope.launch {
                    viewModel.removeRecipe()
                }
            }else{
                scope.launch {
                    viewModel.saveRecipe()
                }
            }
        }

        val recipe = uiState.recipe
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            // recipe image
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(recipe.image)
                    .build(),
                contentDescription = recipe.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(shape = RoundedCornerShape(16.dp)),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // recipe title
            Text(
                text = recipe.title,
                style = MaterialTheme.typography.h6,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Favorite and Saved Icons
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Favorite Icon
                IconButton(
                    onClick = { onFavoriteClick() },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        painter = if(recipeUiState.isFavorite) painterResource(id = R.drawable.full_heart) else painterResource(
                            id = R.drawable.empty_heart
                        ),
                        modifier = Modifier.size(24.dp),
                        contentDescription = if (recipe.isFavorite) "Remove from Favorites" else "Add to Favorites",
                    )
                }

                // Saved Icon
                IconButton(
                    onClick = { onSavedClick() },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = if(recipeUiState.isSaved) painterResource(id = R.drawable.save_fill) else painterResource(id = R.drawable.save_default),
                        contentDescription = if (recipe.isSaved) "Remove from Saved Items" else "Add to Saved Items",
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // recipe details
            Column(modifier = Modifier.fillMaxWidth()) {
                RecipeDetailRow(
                    label = stringResource(R.string.servings),
                    value = recipe.servings.toString()
                )
                RecipeDetailRow(
                    label = stringResource(R.string.ready_in_minutes),
                    value = recipe.readyInMinutes.toString()
                )
                RecipeDetailRow(
                    label = stringResource(R.string.dairy_free),
                    value = if (recipe.dairyFree) stringResource(R.string.yes) else stringResource(R.string.no)
                )
                RecipeDetailRow(
                    label = stringResource(R.string.gluten_free),
                    value = if (recipe.glutenFree) stringResource(R.string.yes) else stringResource(R.string.no)
                )
                RecipeDetailRow(
                    label = stringResource(R.string.vegetarian),
                    value = if (recipe.vegetarian) stringResource(R.string.yes) else stringResource(R.string.no)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ingredients
            Text(
                text = stringResource(R.string.ingredients),
                style = MaterialTheme.typography.h6
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(recipe.extendedIngredients) { ingredient ->
                    IngredientRow(
                        ingredient = ingredient,
                        onAddClick = {
                            Log.i("TAG", "${similarIngredientsList.contains(ingredient)}")
                            Log.i("TAG", "${addedShoppingItemsIngredient.size}")
                            scope.launch { viewModel.addIngredientToShoppingItems(ingredient) }
                        },
                        isSavedIngredient = similarIngredientsList.contains(ingredient),
                        onRemoveClick = {
                            scope.launch { viewModel.removeIngredientFromShoppingItem(ingredient) }
                        }
                    )
                }
            }
        }
    }

}


@Composable
fun RecipeDetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 8.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.subtitle2,
            textAlign = TextAlign.End,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun IngredientRow(
    ingredient: ExtendedIngredient,
    isSavedIngredient: Boolean,
    onAddClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // ingredient icon
        Icon(
            painter = painterResource(id = R.drawable.ingredient),
            contentDescription = stringResource(R.string.ingredient),
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // ingredient name and amount
        Column {
            Text(
                text = ingredient.name,
                style = MaterialTheme.typography.subtitle2
            )
            Text(
                text = stringResource(R.string.ingredient_amount, ingredient.amount.toString(), ingredient.unit),
                style = MaterialTheme.typography.caption,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .weight(1f))

        IconButton(onClick =
        {
            if(isSavedIngredient){
                onRemoveClick()
            }else{
                onAddClick()
            }
        }) {
            Icon(
                painter = if(isSavedIngredient) painterResource(id = R.drawable.check) else painterResource(
                    id = R.drawable.plus_icon
                ),
                modifier = Modifier.size(24.dp),
                contentDescription = null,
            )
        }
    }
}
