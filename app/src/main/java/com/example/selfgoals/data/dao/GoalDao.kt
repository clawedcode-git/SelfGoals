package com.example.selfgoals.data.dao

import androidx.room.*
import com.example.selfgoals.data.entity.Goal
import com.example.selfgoals.data.entity.GoalDetails
import com.example.selfgoals.data.entity.GoalWithCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {
    @Transaction
    @Query("SELECT * FROM goals ORDER BY createdAt DESC")
    fun getAllGoalsWithDetails(): Flow<List<GoalDetails>>

    @Transaction
    @Query("SELECT * FROM goals WHERE id = :id")
    suspend fun getGoalWithCategoryById(id: Long): GoalWithCategory?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: Goal): Long

    @Update
    suspend fun updateGoal(goal: Goal)

    @Delete
    suspend fun deleteGoal(goal: Goal)

    @Query("SELECT * FROM goals WHERE categoryId = :categoryId")
    fun getGoalsByCategory(categoryId: Long): Flow<List<Goal>>
}
