package com.example.hotelapp.presentation.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.hotelapp.core.network.ApiResponse
import com.example.hotelapp.data.repository.RecipeRepository
import com.example.hotelapp.domain.model.Recipe
import com.example.hotelapp.presentation.state.RecipeUiState
import com.example.hotelapp.presentation.viewmodel.RecipeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Rule
import org.junit.Test

class MockRecipeRepository : RecipeRepository {
    override suspend fun getRecipes(): ApiResponse<List<Recipe>> {
        return ApiResponse.Success(emptyList())
    }

    override suspend fun getRecipeById(recipeId: String): ApiResponse<Recipe> {
        return ApiResponse.Error(-1, "Not implemented in mock")
    }
}

class TestRecipeViewModel : RecipeViewModel(MockRecipeRepository()) {
    private val _testUiState = MutableStateFlow<RecipeUiState>(RecipeUiState.Loading)
    override val uiState: StateFlow<RecipeUiState> = _testUiState

    fun setTestState(state: RecipeUiState) {
        _testUiState.value = state
    }
}

class RecipeScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadingState_showsLoadingIndicator() {
        // Given
        composeTestRule.setContent {
            LoadingScreen()
        }

        // Then
        composeTestRule.onNodeWithTag("loading_indicator").assertExists()
    }

    @Test
    fun successState_showsRecipeList() {
        // Given
        val recipes = listOf(
            Recipe(
                id = "1",
                name = "Test Recipe",
                description = "Test Description",
                calories = "500 kcal",
                carbos = "50g",
                difficulty = "Easy",
                fats = "20g",
                headline = "Test Headline",
                image = "test_image.jpg",
                proteins = "30g",
                thumb = "test_thumb.jpg",
                time = "30m"
            )
        )

        // When
        composeTestRule.setContent {
            RecipeList(recipes = recipes)
        }

        // Then
        composeTestRule.onNodeWithText("Test Recipe").assertExists()
        composeTestRule.onNodeWithText("Test Description").assertExists()
    }

    @Test
    fun errorState_showsErrorMessage() {
        // Given
        val errorMessage = "Network Error"

        // When
        composeTestRule.setContent {
            ErrorScreen(message = errorMessage)
        }

        // Then
        composeTestRule.onNodeWithText(errorMessage).assertExists()
    }

    @Test
    fun recipeItem_showsAllRequiredInfo() {
        // Given
        val recipe = Recipe(
            id = "1",
            name = "Test Recipe",
            description = "Test Description",
            calories = "500 kcal",
            carbos = "50g",
            difficulty = "Easy",
            fats = "20g",
            headline = "Test Headline",
            image = "test_image.jpg",
            proteins = "30g",
            thumb = "test_thumb.jpg",
            time = "30m"
        )

        // When
        composeTestRule.setContent {
            RecipeItem(recipe = recipe)
        }

        // Then
        composeTestRule.onNodeWithTag("recipe_item_1").assertExists()
        composeTestRule.onNodeWithTag("recipe_name_1").assertExists()
            .assertTextEquals("Test Recipe")
        composeTestRule.onNodeWithTag("recipe_description_1").assertExists()
            .assertTextEquals("Test Description")
    }

    @Test
    fun recipeScreen_integrationTest() {
        // Given
        val recipes = listOf(
            Recipe(
                id = "1",
                name = "Test Recipe 1",
                description = "Description 1",
                calories = "500 kcal",
                carbos = "50g",
                difficulty = "Easy",
                fats = "20g",
                headline = "Headline 1",
                image = "image1.jpg",
                proteins = "30g",
                thumb = "thumb1.jpg",
                time = "30m"
            ),
            Recipe(
                id = "2",
                name = "Test Recipe 2",
                description = "Description 2",
                calories = "600 kcal",
                carbos = "60g",
                difficulty = "Medium",
                fats = "25g",
                headline = "Headline 2",
                image = "image2.jpg",
                proteins = "35g",
                thumb = "thumb2.jpg",
                time = "45m"
            )
        )

        val testViewModel = TestRecipeViewModel()
        testViewModel.setTestState(RecipeUiState.Success(recipes))

        // When
        composeTestRule.setContent {
            RecipeScreen(viewModel = testViewModel)
        }

        // Then
        composeTestRule.onNodeWithTag("recipe_name_1").assertExists()
            .assertTextEquals("Test Recipe 1")
        composeTestRule.onNodeWithTag("recipe_description_1").assertExists()
            .assertTextEquals("Description 1")
        composeTestRule.onNodeWithTag("recipe_name_2").assertExists()
            .assertTextEquals("Test Recipe 2")
        composeTestRule.onNodeWithTag("recipe_description_2").assertExists()
            .assertTextEquals("Description 2")

        // Scroll to ensure all items are accessible
        composeTestRule.onNodeWithTag("recipe_item_2").performScrollTo()
    }
} 