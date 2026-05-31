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

    @Test
    fun `exportBackupJson generates correct structured JSON`() = runTest {
        val category = com.example.selfgoals.data.entity.Category(id = 10, name = "Health", color = 0xFF00FF)
        val goal = Goal(id = 20, title = "Exercise", description = "Daily exercise", categoryId = 10, deadline = null)
        val milestone = com.example.selfgoals.data.entity.Milestone(id = 30, goalId = 20, title = "Run 5k", position = 0)

        coEvery { repository.getAllCategoriesRaw() } returns listOf(category)
        coEvery { repository.getAllGoalsRaw() } returns listOf(goal)
        coEvery { repository.getAllMilestonesRaw() } returns listOf(milestone)

        viewModel = DashboardViewModel(application, repository, settingsRepository, workManager)
        val json = viewModel.exportBackupJson()

        val gson = com.google.gson.Gson()
        val decoded = gson.fromJson(json, com.example.selfgoals.data.entity.BackupData::class.java)

        assertEquals(1, decoded.categories.size)
        assertEquals("Health", decoded.categories[0].name)
        assertEquals(1, decoded.goals.size)
        assertEquals("Exercise", decoded.goals[0].title)
        assertEquals(1, decoded.milestones.size)
        assertEquals("Run 5k", decoded.milestones[0].title)
    }

    @Test
    fun `importBackupJson restores database correctly when JSON is valid`() = runTest {
        val json = """
            {
              "categories": [{"id": 5, "name": "Work", "color": 123}],
              "goals": [{"id": 6, "title": "Finish task", "description": "", "categoryId": 5}],
              "milestones": [{"id": 7, "goalId": 6, "title": "Step 1", "position": 0, "isCompleted": false}]
            }
        """.trimIndent()

        viewModel = DashboardViewModel(application, repository, settingsRepository, workManager)
        
        coEvery { repository.restoreDatabase(any(), any(), any()) } just Runs

        val result = viewModel.importBackupJson(json)

        assertEquals(true, result)
        coVerify {
            repository.restoreDatabase(
                match { it.size == 1 && it[0].name == "Work" },
                match { it.size == 1 && it[0].title == "Finish task" },
                match { it.size == 1 && it[0].title == "Step 1" }
            )
        }
    }

    @Test
    fun `importBackupJson returns false when JSON is malformed`() = runTest {
        viewModel = DashboardViewModel(application, repository, settingsRepository, workManager)
        val result = viewModel.importBackupJson("invalid json")
        assertEquals(false, result)
    }
}
