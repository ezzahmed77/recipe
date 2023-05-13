package com.example.recipe

import android.app.Application
import com.example.recipe.data.AppContainer
import com.example.recipe.data.DefaultAppContainer
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth

class RecipeApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)

    }
}