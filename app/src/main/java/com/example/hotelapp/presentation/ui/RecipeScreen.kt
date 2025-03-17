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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Star
import com.example.hotelapp.R

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
    LazyColumn( modifier = Modifier.safeContentPadding()) {
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
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            // Recipe Image
            AsyncImage(
                model = recipe.image,
                contentDescription = "Recipe image for ${recipe.name}",
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .testTag("recipe_image_${recipe.id}"),
                contentScale = ContentScale.Crop,
                placeholder = ColorPainter(Color.LightGray),
                error = ColorPainter(Color(0xFFFFE0E0))
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Recipe Details
            Column(
                modifier = Modifier.weight(1f)
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
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    RecipeInfo(
                        icon = Icons.Outlined.Info,
                        text = recipe.time,
                        modifier = Modifier.testTag("recipe_time_${recipe.id}")
                    )
                    RecipeInfo(
                        icon = Icons.Outlined.Star,
                        text = recipe.calories,
                        modifier = Modifier.testTag("recipe_calories_${recipe.id}")
                    )
                }
            }
        }
    }
}

@Composable
private fun RecipeInfo(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall
        )
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
