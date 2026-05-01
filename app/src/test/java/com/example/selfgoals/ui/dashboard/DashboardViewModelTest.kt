package com.example.selfgoals.ui.dashboard

import android.app.Application
import androidx.work.WorkManager
import app.cash.turbine.test
import com.example.selfgoals.data.entity.Goal
import com.example.selfgoals.data.entity.GoalDetails
import com.example.selfgoals.data.repository.GoalRepository
import com.example.selfgoals.data.repository.SettingsRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    private lateinit var viewModel: DashboardViewModel
    private val repository = mockk<GoalRepository>(relaxed = true)
    private val settingsRepository = mockk<SettingsRepository>(relaxed = true)
    private val workManager = mockk<WorkManager>(relaxed = true)
    private val application = mockk<Application>(relaxed = true)
    
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        // Default mock behaviors
        every { repository.getAllGoalsWithDetails() } returns flowOf(emptyList())
        every { repository.getAllCategories() } returns flowOf(emptyList())
        every { settingsRepository.themeMode } returns flowOf(ThemeMode.SYSTEM)
        every { settingsRepository.sortOption } returns flowOf(SortOption.DATE_CREATED)
        every { settingsRepository.showArchived } returns flowOf(false)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `goals flow filters by search query`() = runTest {
        val goal1 = Goal(id = 1, title = "Gym", description = "Morning workout", categoryId = null, deadline = null)
        val goal2 = Goal(id = 2, title = "Read", description = "Finish book", categoryId = null, deadline = null)
        val goals = listOf(
            GoalDetails(goal1, null, emptyList()),
            GoalDetails(goal2, null, emptyList())
        )
        
        every { repository.getAllGoalsWithDetails() } returns flowOf(goals)
        
        viewModel = DashboardViewModel(application, repository, settingsRepository, workManager)
        
        viewModel.updateSearchQuery("Gym")
        
        viewModel.goals.test {
            val result = awaitItem()
            assertEquals(1, result.size)
            assertEquals("Gym", result[0].goal.title)
        }
    }

    @Test
    fun `stats calculation is correct`() = runTest {
        val goal1 = Goal(id = 1, title = "Goal 1", description = "", categoryId = null, deadline = null, isCompleted = true)
        val goal2 = Goal(id = 2, title = "Goal 2", description = "", categoryId = null, deadline = null, isCompleted = false)
        val goals = listOf(
            GoalDetails(goal1, null, emptyList()),
            GoalDetails(goal2, null, emptyList())
        )
        
        every { repository.getAllGoalsWithDetails() } returns flowOf(goals)
        viewModel = DashboardViewModel(application, repository, settingsRepository, workManager)
        
        viewModel.stats.test {
            val result = awaitItem()
            assertEquals(2, result.totalGoals)
            assertEquals(1, result.completedGoals)
            assertEquals(0.5f, result.goalCompletionRate)
        }
    }

    @Test
    fun `toggle goal completion calls repository`() = runTest {
        val goal = Goal(id = 1, title = "Test", description = "", categoryId = null, deadline = null, isCompleted = false)
        
        viewModel = DashboardViewModel(application, repository, settingsRepository, workManager)
        viewModel.toggleGoalCompletion(goal)
        
        coVerify { repository.updateGoal(match { it.id == 1L && it.isCompleted }) }
    }
}
