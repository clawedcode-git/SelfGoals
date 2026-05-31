package com.example.selfgoals.data.repository

import androidx.room.withTransaction
import com.example.selfgoals.data.SelfGoalsDatabase
import com.example.selfgoals.data.dao.CategoryDao
import com.example.selfgoals.data.dao.GoalDao
import com.example.selfgoals.data.dao.MilestoneDao
import com.example.selfgoals.data.entity.Category
import com.example.selfgoals.data.entity.Goal
import com.example.selfgoals.data.entity.GoalDetails
import com.example.selfgoals.data.entity.Milestone
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoalRepository @Inject constructor(
    private val database: SelfGoalsDatabase,
    private val goalDao: GoalDao,
    private val categoryDao: CategoryDao,
    private val milestoneDao: MilestoneDao
) {
    fun getAllGoalsWithDetails(): Flow<List<GoalDetails>> = goalDao.getAllGoalsWithDetails()
    
    suspend fun insertGoal(goal: Goal): Long = goalDao.insertGoal(goal)
    
    suspend fun updateGoal(goal: Goal) = goalDao.updateGoal(goal)
    
    suspend fun deleteGoal(goal: Goal) = goalDao.deleteGoal(goal)

    fun getAllCategories(): Flow<List<Category>> = categoryDao.getAllCategories()
    
    suspend fun insertCategory(category: Category) = categoryDao.insertCategory(category)

    fun getMilestonesForGoal(goalId: Long): Flow<List<Milestone>> = 
        milestoneDao.getMilestonesForGoal(goalId)
        
    suspend fun insertMilestone(milestone: Milestone) = milestoneDao.insertMilestone(milestone)

    suspend fun updateMilestone(milestone: Milestone) = milestoneDao.updateMilestone(milestone)

    suspend fun deleteMilestone(milestone: Milestone) = milestoneDao.deleteMilestone(milestone)

    // Raw list retrievals for backup
    suspend fun getAllCategoriesRaw(): List<Category> = categoryDao.getAllCategoriesRaw()
    suspend fun getAllGoalsRaw(): List<Goal> = goalDao.getAllGoalsRaw()
    suspend fun getAllMilestonesRaw(): List<Milestone> = milestoneDao.getAllMilestonesRaw()

    // Transactional restore to prevent partial imports or database corruption
    suspend fun restoreDatabase(categories: List<Category>, goals: List<Goal>, milestones: List<Milestone>) {
        database.withTransaction {
            milestoneDao.deleteAllMilestones()
            goalDao.deleteAllGoals()
            categoryDao.deleteAllCategories()

            categoryDao.insertCategories(categories)
            goalDao.insertGoals(goals)
            milestoneDao.insertMilestones(milestones)
        }
    }
}
