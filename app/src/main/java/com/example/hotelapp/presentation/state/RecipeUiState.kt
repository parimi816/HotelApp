package com.example.hotelapp.presentation.state
import com.example.hotelapp.domain.model.Recipe

sealed class RecipeUiState {
    object Loading : RecipeUiState()
    data class Success(val recipes: List<Recipe>) : RecipeUiState()
    data class Error(val message: String) : RecipeUiState()
}
