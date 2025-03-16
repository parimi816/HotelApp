package com.example.hotelapp.data.remote.api

import com.example.hotelapp.data.remote.dto.RecipeDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("recipes.json")
    suspend fun getRecipes(): List<RecipeDto>

    @GET("recipes/{id}")
    suspend fun getRecipeById(@Path("id") recipeId: String): RecipeDto
}
