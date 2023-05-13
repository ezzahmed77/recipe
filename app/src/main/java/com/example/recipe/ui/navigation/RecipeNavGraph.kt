package com.example.recipe.ui.navigation

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.recipe.R
import com.example.recipe.ui.screens.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


@Composable
fun RecipeNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
){
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val firebaseAuth = FirebaseAuth.getInstance()

    var currentRoute by remember { mutableStateOf(navController.currentDestination?.route ) }
    Scaffold(

        modifier = modifier,
        topBar={
            if(currentRoute != LogInScreenDestination.route){
                RecipeAppBar(
                    title = currentRoute.toString(),
                    onNavigationIconClick = { scope.launch {  scaffoldState.drawerState.open() }}
                )
            }

        },
        scaffoldState = scaffoldState,
        drawerContent = { DrawerContent(
            onLogout = {
                firebaseAuth.signOut()
                navController.popBackStack(route = LogInScreenDestination.route, inclusive = false)
                scope.launch { scaffoldState.drawerState.close()  }
            },
            email = firebaseAuth.currentUser?.email.toString())
        },
        bottomBar = {
            if(currentRoute != LogInScreenDestination.route){
                BottomAppBar(
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    IconButton(
                        onClick = { navController.navigate(HomeDestination.route) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = stringResource(R.string.saved_recipes),
                            tint = MaterialTheme.colors.onPrimary
                        )
                    }

                    IconButton(
                        onClick = { navController.navigate(RecipesSavedDestination.route) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.save_fill),
                            contentDescription = stringResource(R.string.saved_recipes),
                            tint = MaterialTheme.colors.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    IconButton(
                        onClick = { navController.navigate(RecipesFavoriteDestination.route) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = stringResource(R.string.favorite_recipes),
                            tint = MaterialTheme.colors.onPrimary
                        )
                    }
                    IconButton(
                        onClick = { navController.navigate(ShoppingListDestination.route)},
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ShoppingCart,
                            contentDescription = stringResource(R.string.shopping_list),
                            tint = MaterialTheme.colors.onPrimary
                        )
                    }
                }
            }
        },
    ) {
        DisposableEffect(key1 = navController) {
            val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
                currentRoute = destination.route
            }
            navController.addOnDestinationChangedListener(listener)
            onDispose {
                navController.removeOnDestinationChangedListener(listener)
            }
        }
        NavHost(
            modifier = modifier.padding(it),
            navController = navController,
            startDestination = LogInScreenDestination.route
        ) {
            composable(route = LogInScreenDestination.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(
                            HomeDestination.route,
                            navOptions = NavOptions.Builder()
                                .setPopUpTo(LogInScreenDestination.route, inclusive = true)
                                .build()
                        )}
                )
            }
            // HomeScreen
            composable(route = HomeDestination.route){
                HomeScreen(navigateToRecipeDetail = {
                    navController.navigate("${RecipeDetailsDestination.route}/${it}")
                })
            }
            // RecipeDetailScreen
            composable(
                route = RecipeDetailsDestination.routeWithArgs,
                arguments = listOf(navArgument(RecipeDetailsDestination.itemIdArg) {
                    type = NavType.IntType
                })
            ) {
                RecipeDetailsScreen(navigateUp = { navController.navigateUp() })
            }
            // RecipesFavoriteScreen
            composable(route = RecipesFavoriteDestination.route) {
                RecipesFavoriteScreen(
                    navigateUp = { navController.navigateUp() },
                    navigateToRecipeDetail = {
                        navController.navigate("${RecipeDetailsDestination.route}/${it}")
                    }
                )
            }
            // RecipesSavedScreen
            composable(route = RecipesSavedDestination.route) {
                RecipesSavedScreen(
                    navigateUp = { navController.navigateUp() },
                    navigateToRecipeDetail = {
                        navController.navigate("${RecipeDetailsDestination.route}/${it}")
                    }
                )
            }
            // ShoppingItemsScreen
            composable(route = ShoppingListDestination.route) {
                ShoppingItemsScreen()
            }

        }
    }

}

@Composable
fun DrawerContent(
    email: String ="",
    onLogout: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Add items to the drawer
        Text(text = email, modifier = Modifier.padding(16.dp))
        Divider(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp))
        // Add the logout button
        Button(
            onClick = {

                onLogout() },
            modifier = Modifier
                .padding(16.dp)
                .align(alignment = Alignment.Start)
        ) {
            Text(text = "Logout")
        }
    }
}

@Composable
fun RecipeAppBar(
    title: String,
    onNavigationIconClick: () -> Unit
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = { onNavigationIconClick() }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
        }
    )
}
