package com.example.selfgoals.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.selfgoals.data.dao.CategoryDao
import com.example.selfgoals.data.dao.GoalDao
import com.example.selfgoals.data.dao.MilestoneDao
import com.example.selfgoals.data.entity.Category
import com.example.selfgoals.data.entity.Goal
import com.example.selfgoals.data.entity.Milestone

@Database(
    entities = [Goal::class, Category::class, Milestone::class],
    version = 1,
    exportSchema = false
)
abstract class SelfGoalsDatabase : RoomDatabase() {
    abstract fun goalDao(): GoalDao
    abstract fun categoryDao(): CategoryDao
    abstract fun milestoneDao(): MilestoneDao
}
