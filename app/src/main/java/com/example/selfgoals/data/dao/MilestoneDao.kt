package com.example.selfgoals.data.dao

import androidx.room.*
import com.example.selfgoals.data.entity.Milestone
import kotlinx.coroutines.flow.Flow

@Dao
interface MilestoneDao {
    @Query("SELECT * FROM milestones WHERE goalId = :goalId ORDER BY position ASC")
    fun getMilestonesForGoal(goalId: Long): Flow<List<Milestone>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilestone(milestone: Milestone): Long

    @Update
    suspend fun updateMilestone(milestone: Milestone)

    @Delete
    suspend fun deleteMilestone(milestone: Milestone)
}
