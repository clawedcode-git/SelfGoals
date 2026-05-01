package com.example.selfgoals.data.repository

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
}
