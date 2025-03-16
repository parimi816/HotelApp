package com.example.hotelapp.data.remote.api

import com.example.hotelapp.data.remote.dto.RecipeDto
import com.google.gson.Gson
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiServiceTest {
    private lateinit var apiService: ApiService
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getRecipes returns successful response`() = runTest {
        // Given
        val mockRecipe = RecipeDto(
            calories = "500 kcal",
            carbos = "47 g",
            description = "Delicious recipe",
            difficulty = "0",
            fats = "8 g",
            headline = "Test headline",
            id = "123",
            image = "image_url",
            name = "Test Recipe",
            proteins = "43 g",
            thumb = "thumb_url",
            time = "PT35M"
        )
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(Gson().toJson(listOf(mockRecipe)))
        mockWebServer.enqueue(mockResponse)

        // When
        val response = apiService.getRecipes()

        // Then
        assertEquals(1, response.size)
        assertEquals("123", response[0].id)
        assertEquals("Test Recipe", response[0].name)
    }

    @Test
    fun `getRecipes handles empty response`() = runTest {
        // Given
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("[]")
        mockWebServer.enqueue(mockResponse)

        // When
        val response = apiService.getRecipes()

        // Then
        assertEquals(0, response.size)
    }

    @Test
    fun `getRecipes handles error response`() = runTest {
        // Given
        val mockResponse = MockResponse()
            .setResponseCode(404)
            .setBody("{\"error\": \"Not found\"}")
        mockWebServer.enqueue(mockResponse)

        // When/Then
        try {
            apiService.getRecipes()
        } catch (e: Exception) {
            assertEquals(404, (e as retrofit2.HttpException).code())
        }
    }

    // Tests loading state
    fun loadingState_showsLoadingIndicator()
    
    // Tests successful state with recipe list
    fun successState_showsRecipeList()
    
    // Tests error state
    fun errorState_showsErrorMessage()
    
    // Tests individual recipe item display
    fun recipeItem_showsAllRequiredInfo()
    
    // Tests full screen with multiple recipes
    fun recipeScreen_integrationTest()
}