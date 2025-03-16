package com.example.hotelapp.presentation.viewmodel

import com.example.hotelapp.data.repository.RecipeRepository
import com.example.hotelapp.presentation.state.RecipeUiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotelapp.core.network.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class RecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<RecipeUiState>(RecipeUiState.Loading)
    open val uiState: StateFlow<RecipeUiState> = _uiState

    init {
        loadRecipes()
    }

    fun loadRecipes() {
        viewModelScope.launch {
            _uiState.value = RecipeUiState.Loading
            when (val result = recipeRepository.getRecipes()) {
                is ApiResponse.Success -> _uiState.value = RecipeUiState.Success(result.data)
                is ApiResponse.Error -> _uiState.value = RecipeUiState.Error(result.message ?: "Unknown error occurred")
                ApiResponse.NetworkError -> _uiState.value = RecipeUiState.Error("Network error occurred")
            }
        }
    }
}
