package com.example.hotelapp.data.repository

import android.Manifest
import androidx.annotation.RequiresPermission
import com.example.hotelapp.core.network.ApiResponse
import com.example.hotelapp.core.network.NetworkHandler
import com.example.hotelapp.data.mapper.toDomain
import com.example.hotelapp.data.remote.api.ApiService
import com.example.hotelapp.domain.model.Recipe
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val networkHandler: NetworkHandler
) : RecipeRepository {

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override suspend fun getRecipes(): ApiResponse<List<Recipe>> {
        return if (!networkHandler.isNetworkAvailable()) {
            ApiResponse.NetworkError
        } else {
            try {
                val response = apiService.getRecipes()
                ApiResponse.Success(response.map { it.toDomain() })
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    override suspend fun getRecipeById(recipeId: String): ApiResponse<Recipe> {
        return try {
            if (!networkHandler.isNetworkAvailable()) {
                return ApiResponse.NetworkError
            }
            val response = apiService.getRecipeById(recipeId)
            ApiResponse.Success(response.toDomain())
        } catch (e: Exception) {
            handleException(e)
        }
    }

    private fun handleException(e: Exception): ApiResponse.Error {
        return when (e) {
            is HttpException -> ApiResponse.Error(e.code(), e.message())
            is IOException -> ApiResponse.Error(-1, "Network Error")
            else -> ApiResponse.Error(-1, "Unknown Error")
        }
    }
}
