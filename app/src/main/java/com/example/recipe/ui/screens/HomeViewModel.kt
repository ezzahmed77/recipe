package com.example.recipe.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipe.data.local.model.RecipeLocalDetail
import com.example.recipe.data.remote.model.RecipeRandom
import com.example.recipe.data.remote.model.RecipeRemoteBasic
import com.example.recipe.domain.RecipesDefaultRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed interface RecipeSuggestedUiState{
    data class Success(val recipes: List<RecipeRandom>): RecipeSuggestedUiState
    object Loading: RecipeSuggestedUiState
    data class Error(val message: String): RecipeSuggestedUiState
}

sealed interface RecipeSearchUiState{
    data class Success(val recipes: List<RecipeRemoteBasic>): RecipeSearchUiState
    object Loading: RecipeSearchUiState
    data class Error(val message: String): RecipeSearchUiState

}

sealed interface RecipeLocalUiState{
    data class Success(val recipes: List<RecipeLocalDetail>): RecipeLocalUiState
    object Loading: RecipeLocalUiState
    data class Error(val message: String): RecipeLocalUiState

}


const val api_key = "5e610ff45567459e921e97221bb4451a"
class HomeViewModel(
    private val defaultRepository: RecipesDefaultRepository
): ViewModel() {

    // Search Query
    var searchQuery by mutableStateOf("")

    var recipeSuggestedUiState: RecipeSuggestedUiState by mutableStateOf(RecipeSuggestedUiState.Loading)
        private set

    var recipeSearchUiState: RecipeSearchUiState by mutableStateOf(RecipeSearchUiState.Loading)
        private set

    var recipeLocalUiState: StateFlow<RecipeLocalUiState> = defaultRepository.getAllRecipes().map {
        RecipeLocalUiState.Success(recipes = it)
    }.catch {
        RecipeLocalUiState.Error(message = it.message.toString())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = RecipeLocalUiState.Loading
    )


    init {
        getData()
    }

    private fun getData() {
        // Get Suggested Recipes
        viewModelScope.launch {
            try{
                val response = defaultRepository.getRandomRecipes(api_key, 15)
                recipeSuggestedUiState = RecipeSuggestedUiState.Success(recipes = response)

            } catch (e: Exception){
                recipeSuggestedUiState = RecipeSuggestedUiState.Error(message = e.message.toString())
            }
        }
    }

    fun getSearchRecipes(){
        viewModelScope.launch {
            try{
                val recipesResultList = defaultRepository.getSearchRecipes(apiKey = api_key, query = searchQuery)
                recipeSearchUiState = RecipeSearchUiState.Success(recipes = recipesResultList)
            }catch (e: java.lang.Exception){
                recipeSearchUiState = RecipeSearchUiState.Error(message = e.message.toString())
            }

        }
    }

    fun updateQuery(queryUpdated: String){
        searchQuery = queryUpdated
    }

}
