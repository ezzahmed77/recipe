package com.example.recipe.utilites.convertors

import com.example.recipe.data.local.model.RecipeLocalBasic
import com.example.recipe.data.local.model.RecipeLocalDetail
import com.example.recipe.data.remote.model.RecipeRemoteBasic
import com.example.recipe.data.remote.model.RecipeRemoteDetail

fun RecipeRemoteBasic.toRecipeLocalBasic(
    isFavorite: Boolean = false,
    isSaved: Boolean = false,
): RecipeLocalBasic{
    return RecipeLocalBasic(
        id = id,
        title = title,
        image = image,
        imageType = imageType,
        isFavorite = isFavorite,
        isSaved = isSaved
    )
}

fun RecipeLocalBasic.toRecipeRemoteBasic(): RecipeRemoteBasic {
    return RecipeRemoteBasic(
        id = id,
        title = title,
        image = image,
        imageType = imageType
    )
}

fun RecipeRemoteDetail.toRecipeLocalDetail(
    isFavorite: Boolean = false,
    isSaved: Boolean = false,
): RecipeLocalDetail{

    return RecipeLocalDetail(
        id = id,
        title = title,
        image = image,
        imageType = imageType,
        servings = servings,
        readyInMinutes = readyInMinutes,
        dairyFree = dairyFree,
        glutenFree = glutenFree,
        vegetarian = vegetarian,
        extendedIngredients = extendedIngredients,
        isFavorite = isFavorite,
        isSaved = isSaved
    )
}

fun RecipeLocalDetail.toRecipeRemoteDetail(): RecipeRemoteDetail {
    return RecipeRemoteDetail(
        id = id,
        title = title,
        image = image,
        imageType = imageType,
        servings = servings,
        readyInMinutes = readyInMinutes,
        dairyFree = dairyFree,
        glutenFree = glutenFree,
        vegetarian = vegetarian,
        extendedIngredients = extendedIngredients,
    )
}

fun getEmptyRecipeLocalDetail(): RecipeLocalDetail{
    return RecipeLocalDetail(
        id = 1,
        title = "title",
        image = "image",
        imageType = "imageType",
        servings = 2,
        readyInMinutes = 33,
        dairyFree = false,
        glutenFree = false,
        vegetarian = false,
        extendedIngredients = emptyList(),
        isFavorite = false,
        isSaved = false
    )
}