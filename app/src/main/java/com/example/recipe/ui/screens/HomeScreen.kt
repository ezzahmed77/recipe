package com.example.recipe.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.recipe.AppViewModelProvider
import com.example.recipe.R
import com.example.recipe.data.local.model.RecipeLocalDetail
import com.example.recipe.data.remote.model.RecipeRandom
import com.example.recipe.data.remote.model.RecipeRemoteBasic
import com.example.recipe.ui.navigation.NavigationDestination
import com.google.firebase.auth.FirebaseAuth

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

enum class HomeState { Search, Normal }
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToRecipeDetail: (Int) -> Unit,
    viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = AppViewModelProvider.Factory)
) {
    var homeState by remember { mutableStateOf(HomeState.Normal) }

    val searchUiState = viewModel.recipeSearchUiState
    val suggestedUiState = viewModel.recipeSuggestedUiState
    val recipeLocalUiState = viewModel.recipeLocalUiState.collectAsState().value

    Column(modifier = modifier
        .fillMaxSize()
        .padding(8.dp)
    ) {
        SearchBar(
            query = viewModel.searchQuery,
            onQueryChange = {
                homeState = if(it.isNotEmpty()){
                    HomeState.Search
                } else {
                    HomeState.Normal
                }
            viewModel.updateQuery(it) },
            onSearch = { viewModel.getSearchRecipes() }
        )
        if(homeState == HomeState.Search){
            if(searchUiState is RecipeSearchUiState.Success){
                SearchedRecipes(
                    recipes = searchUiState.recipes,
                    onRecipeClick = {
                        navigateToRecipeDetail(it.id)
                    })
            }
        } else if(homeState == HomeState.Normal) {
            Column() {
                // Show Saved Recipes
                if(recipeLocalUiState is RecipeLocalUiState.Success){
                    SavedRecipes(
                        recipes = recipeLocalUiState.recipes,
                        onRecipeClick = { navigateToRecipeDetail(it.id)}
                    )
                }
                // Show Suggested Random Recipes
                if(suggestedUiState is RecipeSuggestedUiState.Success){
                    SuggestedRecipes(
                        recipes = suggestedUiState.recipes,
                        onRecipeClick = {navigateToRecipeDetail(it.id) }
                    )
                }
            }
        }
    }

}


@Composable
fun SearchedRecipes(
    modifier: Modifier = Modifier,
    recipes: List<RecipeRemoteBasic>,
    onRecipeClick: (RecipeRemoteBasic) -> Unit
) {
    Column(modifier = modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
        Text(
            text = stringResource(id = R.string.search_result),
            style = MaterialTheme.typography.h5
        )
        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth(),
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(recipes){
                SearchedRecipeItem(recipe = it, onRecipeClick = onRecipeClick)
            }
        }

    }
}

@Composable
fun SearchedRecipeItem(
    modifier: Modifier = Modifier,
    recipe: RecipeRemoteBasic,
    onRecipeClick: (RecipeRemoteBasic) -> Unit
) {
    Card(
        modifier = modifier
            .wrapContentSize(align = Alignment.Center)
            .clickable { onRecipeClick(recipe) },
        elevation = 4.dp,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(recipe.image)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
                    .clip(shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                    .padding(bottom = 8.dp)
            )
            Text(
                text = recipe.title,
                fontSize = 12.sp,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic,
                maxLines = 2
            )
        }
    }
}

@Composable
fun SuggestedRecipes(
    modifier: Modifier = Modifier,
    recipes: List<RecipeRandom>,
    onRecipeClick: (RecipeRandom) -> Unit
) {
    Column(modifier = modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
        Text(
            text = stringResource(id = R.string.recipes_you_may_like),
            style = MaterialTheme.typography.h4,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Cursive,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth(),
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(recipes){
                SuggestedRecipeItem(recipe = it, onRecipeClick = onRecipeClick)
            }
        }

    }

}

@Composable
fun SuggestedRecipeItem(
    modifier: Modifier = Modifier,
    recipe: RecipeRandom,
    onRecipeClick: (RecipeRandom) -> Unit
) {
    Card(
        modifier = modifier
            .clickable { onRecipeClick(recipe) },
        elevation = 4.dp,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(recipe.image)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
                    .clip(shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                    .padding(bottom = 8.dp)
            )
            Text(
                text = recipe.title,
                fontSize = 12.sp,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic,
                maxLines = 2
            )
        }
    }
}


@Composable
fun SavedRecipes(
    modifier: Modifier = Modifier,
    recipes: List<RecipeLocalDetail>,
    onRecipeClick: (RecipeLocalDetail) -> Unit
){
    Column(modifier = modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
        Text(
            text = stringResource(id = R.string.my_recipes),
            style = MaterialTheme.typography.h4,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Cursive
        )
        Spacer(modifier = Modifier.height(16.dp))
        if(recipes.isEmpty()){
            Text(
                text = stringResource(id = R.string.saved_recipes_empty),
                fontSize = 16.sp,
                fontWeight = FontWeight.Light,
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.Cursive,
                modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
            )
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(recipes){
                RecipeLocalItem(recipe = it, onRecipeClick = onRecipeClick)
            }
        }

    }
}

@Composable
fun RecipeLocalItem(
    modifier: Modifier = Modifier, 
    recipe: RecipeLocalDetail,
    onRecipeClick: (RecipeLocalDetail) -> Unit
) {

    Column(
        modifier = modifier
            .wrapContentSize(align = Alignment.Center)
            .clickable { onRecipeClick(recipe) },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(recipe.image)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
        )
        Text(
            text = recipe.title,
            fontSize = 14.sp,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(48.dp)
        )
    }


}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    TextField(
        value = query,
        onValueChange = { onQueryChange(it) },
        modifier = Modifier.fillMaxWidth(),
        textStyle = MaterialTheme.typography.body1,
        label = { Text(text = "Search") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = androidx.compose.ui.text.input.ImeAction.Search,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(onSearch = {
            focusManager.clearFocus()
            onSearch()
        })
    )
}