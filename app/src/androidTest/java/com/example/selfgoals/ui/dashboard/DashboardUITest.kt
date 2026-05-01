package com.example.selfgoals.ui.dashboard

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.selfgoals.MainActivity
import com.example.selfgoals.ui.theme.SelfGoalsTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class DashboardUITest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun dashboard_header_is_displayed() {
        // Hilt will provide the real ViewModel and dependencies
        composeTestRule.onNodeWithText("SelfGoals").assertIsDisplayed()
    }

    @Test
    fun clicking_new_goal_opens_dialog() {
        composeTestRule.onNodeWithText("New Goal").performClick()
        composeTestRule.onNodeWithText("What's the goal?").assertIsDisplayed()
    }
}
