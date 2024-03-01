package com.example.recipeapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


@Composable
fun RecipeApp(navController: NavHostController){
    val recipeViewModel: MainViewModel = viewModel()
    val viewState by recipeViewModel.categoriesState
    val screenName by recipeViewModel.screenTitle

    Scaffold(topBar = {
        RecipesAppTopBar(screenTitle = screenName, navController = navController, canPop = screenName != "Recipes")
    }) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            NavHost(navController = navController, startDestination = Screen.RecipeScreen.route){
                composable(route = Screen.RecipeScreen.route){
                    RecipeScreen(navigateToDetail = {
                        navController.currentBackStackEntry?.savedStateHandle?.set("cat", it)
                        navController.navigate(Screen.DetailScreen.route)
                    }, viewState = viewState, viewModel = recipeViewModel)
                }

                composable(route = Screen.DetailScreen.route){
                    val category = navController.previousBackStackEntry?.savedStateHandle?.get<Category>("cat") ?:
                    Category("", "", "", "")

                    CategoryDetailScreen(category = category, viewModel = recipeViewModel)
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun RecipesAppTopBar(canPop: Boolean = false, screenTitle: String, navController: NavHostController){
    if (canPop){
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.primaryContainer
            ),
            title = {
                Text(screenTitle)
            },
            navigationIcon = {
                IconButton(onClick = {
                    navController.navigateUp()
                }) {
                    Icon(Icons.Rounded.ArrowBack, "", tint = MaterialTheme.colorScheme.primaryContainer)
                }
            }
        )
    }else{
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.primaryContainer,
            ),
            title = {
                Text(screenTitle)
            }
        )
    }
}