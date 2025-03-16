package com.example.hotelapp.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.hotelapp.domain.model.Recipe
import com.example.hotelapp.presentation.state.RecipeUiState
import com.example.hotelapp.presentation.viewmodel.RecipeViewModel
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun RecipeScreen(
    viewModel: RecipeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is RecipeUiState.Loading -> LoadingScreen()
        is RecipeUiState.Success -> RecipeList(recipes = state.recipes)
        is RecipeUiState.Error -> ErrorScreen(message = state.message)
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.testTag("loading_indicator")
        )
    }
}

@Composable
fun RecipeList(recipes: List<Recipe>) {
    LazyColumn {
        items(recipes) { recipe ->
            RecipeItem(recipe)
        }
    }
}

@Composable
fun RecipeItem(recipe: Recipe) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .testTag("recipe_item_${recipe.id}")
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = recipe.name,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.testTag("recipe_name_${recipe.id}")
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = recipe.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.testTag("recipe_description_${recipe.id}")
            )
        }
    }
}

@Composable
fun ErrorScreen(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.testTag("error_message")
        )
    }
}
