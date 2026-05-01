package com.example.selfgoals.di

import android.content.Context
import androidx.room.Room
import com.example.selfgoals.data.SelfGoalsDatabase
import com.example.selfgoals.data.dao.CategoryDao
import com.example.selfgoals.data.dao.GoalDao
import com.example.selfgoals.data.dao.MilestoneDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SelfGoalsDatabase {
        return Room.databaseBuilder(
            context,
            SelfGoalsDatabase::class.java,
            "self_goals_db"
        ).build()
    }

    @Provides
    fun provideGoalDao(db: SelfGoalsDatabase): GoalDao = db.goalDao()

    @Provides
    fun provideCategoryDao(db: SelfGoalsDatabase): CategoryDao = db.categoryDao()

    @Provides
    fun provideMilestoneDao(db: SelfGoalsDatabase): MilestoneDao = db.milestoneDao()
}
