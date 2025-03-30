package pt.pedromarques.vehicleauction

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import pt.pedromarques.vehicleauction.home.HomeScreen
import pt.pedromarques.vehicleauction.home.HomeUiState
import pt.pedromarques.vehicleauction.home.SingleChoiceSegmentedButton

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // check screen launch and loading view is present
    @Test
    fun loadingTest() {
        val fakeUiState = HomeUiState(isLoading = true)

        composeTestRule.setContent {
            HomeScreen(uiState = fakeUiState, updateModelsList = {}, goToSearchResults = { _, _, _, _, _ -> }, goToFavourites = {})
        }

        composeTestRule.onNodeWithText("Loading cars").assertIsDisplayed()
    }

    // Example of test in a single component
    @Test
    fun selectNumberItemsToDisplayTest() {

        composeTestRule.setContent {
            Column{
                SingleChoiceSegmentedButton(
                    modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
                    selected = {

                    })
            }
        }
        composeTestRule.onNodeWithText("10").assertIsDisplayed().assertIsSelected()
        composeTestRule.onNodeWithText("25").performClick()
        composeTestRule.onNodeWithText("25").assertIsDisplayed().assertIsSelected()
        composeTestRule.onNodeWithText("10").assertIsDisplayed().assertIsNotSelected()
    }

    // Example of testing features, in this case select a make from the list
    @Test
    fun selectMakeTest() {
        val fakeUiState = HomeUiState(isLoading = false, selectedMake = "", makers = listOf("Audi", "Ford", "Toyota"))

        composeTestRule.setContent {
            HomeScreen(uiState = fakeUiState, updateModelsList = {}, goToSearchResults = { _, _, _, _, _ -> }, goToFavourites = {})
        }

        //check Ford make is not selected
        composeTestRule.onNodeWithText("Ford").assertIsNotDisplayed()
        // check make selector exists and perform click
        composeTestRule.onNodeWithText("Make").assertIsDisplayed().performClick()
        // check selector model is open
        composeTestRule.onNodeWithTag("modal").isDisplayed()
        // select second item
        composeTestRule.onNodeWithTag("modalItem_1").performClick()
        // check modal is closed
        composeTestRule.onNodeWithTag("modal").isNotDisplayed()
        //check Ford make is now selected (2nd item of mock list)
        composeTestRule.onNodeWithText("Ford").assertIsDisplayed()
    }
}