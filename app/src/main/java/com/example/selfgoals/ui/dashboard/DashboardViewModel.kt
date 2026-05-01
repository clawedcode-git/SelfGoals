package com.example.selfgoals.ui.dashboard

import android.app.Application
import com.example.selfgoals.R
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.selfgoals.data.entity.*
import com.example.selfgoals.data.repository.GoalRepository
import com.example.selfgoals.data.repository.SettingsRepository
import com.example.selfgoals.worker.GoalReminderWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

enum class SortOption {
    DATE_CREATED, DEADLINE, PROGRESS, NAME, PRIORITY
}

enum class ThemeMode {
    SYSTEM, LIGHT, DARK
}

@HiltViewModel
class DashboardViewModel @Inject constructor(
    application: Application,
    private val repository: GoalRepository,
    private val settingsRepository: SettingsRepository,
    private val workManager: WorkManager
) : AndroidViewModel(application) {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val showArchived: StateFlow<Boolean> = settingsRepository.showArchived
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val _selectedCategoryId = MutableStateFlow<Long?>(null)
    val selectedCategoryId: StateFlow<Long?> = _selectedCategoryId.asStateFlow()

    val sortOption: StateFlow<SortOption> = settingsRepository.sortOption
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SortOption.DATE_CREATED)

    val themeMode: StateFlow<ThemeMode> = settingsRepository.themeMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ThemeMode.SYSTEM)

    private val _allGoals = repository.getAllGoalsWithDetails()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val goals: StateFlow<List<GoalDetails>> = combine(
        _allGoals, 
        _searchQuery, 
        showArchived, 
        _selectedCategoryId,
        sortOption
    ) { goalsList, query, archived, selectedId, sort ->
        val filtered = goalsList.filter { 
            it.goal.isArchived == archived &&
            (selectedId == null || it.goal.categoryId == selectedId) &&
            (query.isBlank() || 
             it.goal.title.contains(query, ignoreCase = true) || 
             it.goal.description.contains(query, ignoreCase = true) ||
             it.category?.name?.contains(query, ignoreCase = true) == true)
        }

        when (sort) {
            SortOption.DATE_CREATED -> filtered.sortedByDescending { it.goal.createdAt }
            SortOption.DEADLINE -> filtered.sortedBy { it.goal.deadline ?: Long.MAX_VALUE }
            SortOption.NAME -> filtered.sortedBy { it.goal.title.lowercase() }
            SortOption.PRIORITY -> filtered.sortedByDescending { it.goal.isPriority }
            SortOption.PROGRESS -> filtered.sortedByDescending { 
                if (it.milestones.isEmpty()) 0f else it.milestones.count { m -> m.isCompleted }.toFloat() / it.milestones.size
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val categories: StateFlow<List<Category>> = repository.getAllCategories()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val stats: StateFlow<ProgressStats> = combine(_allGoals, categories) { goalsList, categoriesList ->
        val activeGoals = goalsList.filter { !it.goal.isArchived }
        val totalGoals = activeGoals.size
        val completedGoals = activeGoals.count { it.goal.isCompleted }
        
        val allMilestones = activeGoals.flatMap { it.milestones }
        val totalMilestones = allMilestones.size
        val completedMilestones = allMilestones.count { it.isCompleted }

        val categoryStats = categoriesList.associate { category ->
            val categoryGoals = activeGoals.filter { it.goal.categoryId == category.id }
            val rate = if (categoryGoals.isNotEmpty()) {
                categoryGoals.count { it.goal.isCompleted }.toFloat() / categoryGoals.size
            } else 0f
            category.name to rate
        }

        ProgressStats(
            totalGoals = totalGoals,
            completedGoals = completedGoals,
            totalMilestones = totalMilestones,
            completedMilestones = completedMilestones,
            categoryStats = categoryStats
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProgressStats()
    )

    fun updateSearchQuery(query: String) { _searchQuery.value = query }
    fun toggleShowArchived() { 
        viewModelScope.launch { settingsRepository.setShowArchived(!showArchived.value) }
    }
    fun selectCategory(categoryId: Long?) { _selectedCategoryId.value = if (_selectedCategoryId.value == categoryId) null else categoryId }
    fun setSortOption(option: SortOption) { 
        viewModelScope.launch { settingsRepository.setSortOption(option) }
    }
    fun setThemeMode(mode: ThemeMode) { 
        viewModelScope.launch { settingsRepository.setThemeMode(mode) }
    }

    fun addGoal(title: String, description: String, categoryId: Long? = null, reminderTime: Long? = null, deadline: Long? = null, isPriority: Boolean = false) {
        viewModelScope.launch {
            val goalId = repository.insertGoal(
                Goal(
                    title = title,
                    description = description,
                    categoryId = categoryId,
                    deadline = deadline,
                    reminderTime = reminderTime,
                    isPriority = isPriority
                )
            )
            reminderTime?.let { scheduleReminder(goalId, title, it) }
        }
    }

    fun editGoal(goal: Goal, reminderTimeChanged: Boolean) {
        viewModelScope.launch {
            repository.updateGoal(goal)
            if (reminderTimeChanged) {
                cancelReminder(goal.id)
                goal.reminderTime?.let { scheduleReminder(goal.id, goal.title, it) }
            }
        }
    }

    fun toggleGoalCompletion(goal: Goal) {
        viewModelScope.launch { repository.updateGoal(goal.copy(isCompleted = !goal.isCompleted)) }
    }

    fun toggleArchive(goal: Goal) {
        viewModelScope.launch { repository.updateGoal(goal.copy(isArchived = !goal.isArchived)) }
    }

    fun togglePriority(goal: Goal) {
        viewModelScope.launch { repository.updateGoal(goal.copy(isPriority = !goal.isPriority)) }
    }

    fun addMilestone(goalId: Long, title: String) {
        viewModelScope.launch {
            val currentMilestones = _allGoals.value.find { it.goal.id == goalId }?.milestones ?: emptyList()
            repository.insertMilestone(Milestone(goalId = goalId, title = title, position = currentMilestones.size))
        }
    }

    fun toggleMilestoneCompletion(milestone: Milestone) {
        viewModelScope.launch { repository.updateMilestone(milestone.copy(isCompleted = !milestone.isCompleted)) }
    }

    fun reorderMilestone(goalId: Long, fromIndex: Int, toIndex: Int) {
        viewModelScope.launch {
            val details = _allGoals.value.find { it.goal.id == goalId } ?: return@launch
            val milestones = details.milestones.toMutableList()
            if (fromIndex !in milestones.indices || toIndex !in milestones.indices) return@launch
            
            val item = milestones.removeAt(fromIndex)
            milestones.add(toIndex, item)
            
            milestones.forEachIndexed { index, milestone ->
                repository.updateMilestone(milestone.copy(position = index))
            }
        }
    }

    private fun scheduleReminder(goalId: Long, title: String, reminderTime: Long) {
        val delay = reminderTime - System.currentTimeMillis()
        if (delay > 0) {
            val workRequest = OneTimeWorkRequestBuilder<GoalReminderWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(Data.Builder().putLong("goal_id", goalId).putString("goal_title", title).build())
                .addTag("goal_reminder_$goalId")
                .build()
            workManager.enqueue(workRequest)
        }
    }

    private fun cancelReminder(goalId: Long) {
        workManager.cancelAllWorkByTag("goal_reminder_$goalId")
    }

    fun generateExportText(): String {
        val goalsList = _allGoals.value
        if (goalsList.isEmpty()) return getApplication<Application>().getString(R.string.export_no_goals)
        val sb = StringBuilder()
        val header = getApplication<Application>().getString(
            R.string.export_header,
            SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())
        )
        sb.append(header)
        goalsList.forEachIndexed { index, details ->
            val g = details.goal
            val status = getApplication<Application>().getString(
                if (g.isArchived) R.string.status_archived 
                else if (g.isCompleted) R.string.status_done 
                else R.string.status_active
            )
            val priority = if (g.isPriority) " ⭐" else ""
            sb.append(getApplication<Application>().getString(R.string.export_goal_item, index + 1, status, g.title, priority))
            if (g.description.isNotEmpty()) sb.append("   ${g.description}\n")
            details.milestones.forEach { m -> sb.append("   ${if (m.isCompleted) "✓" else "○"} ${m.title}\n") }
            sb.append("\n")
        }
        return sb.toString()
    }

    fun deleteGoal(goal: Goal) {
        viewModelScope.launch {
            cancelReminder(goal.id)
            repository.deleteGoal(goal)
        }
    }

    fun addCategory(name: String, color: Int) {
        viewModelScope.launch { repository.insertCategory(Category(name = name, color = color)) }
    }
}
