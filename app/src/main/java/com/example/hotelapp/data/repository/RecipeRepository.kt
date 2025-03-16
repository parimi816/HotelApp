package com.example.hotelapp.data.repository

import com.example.hotelapp.core.network.ApiResponse
import com.example.hotelapp.domain.model.Recipe

interface RecipeRepository {
    suspend fun getRecipes(): ApiResponse<List<Recipe>>
    suspend fun getRecipeById(recipeId: String): ApiResponse<Recipe>
}