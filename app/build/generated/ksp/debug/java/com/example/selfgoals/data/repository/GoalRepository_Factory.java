package com.example.selfgoals.data.repository;

import com.example.selfgoals.data.dao.CategoryDao;
import com.example.selfgoals.data.dao.GoalDao;
import com.example.selfgoals.data.dao.MilestoneDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class GoalRepository_Factory implements Factory<GoalRepository> {
  private final Provider<GoalDao> goalDaoProvider;

  private final Provider<CategoryDao> categoryDaoProvider;

  private final Provider<MilestoneDao> milestoneDaoProvider;

  public GoalRepository_Factory(Provider<GoalDao> goalDaoProvider,
      Provider<CategoryDao> categoryDaoProvider, Provider<MilestoneDao> milestoneDaoProvider) {
    this.goalDaoProvider = goalDaoProvider;
    this.categoryDaoProvider = categoryDaoProvider;
    this.milestoneDaoProvider = milestoneDaoProvider;
  }

  @Override
  public GoalRepository get() {
    return newInstance(goalDaoProvider.get(), categoryDaoProvider.get(), milestoneDaoProvider.get());
  }

  public static GoalRepository_Factory create(Provider<GoalDao> goalDaoProvider,
      Provider<CategoryDao> categoryDaoProvider, Provider<MilestoneDao> milestoneDaoProvider) {
    return new GoalRepository_Factory(goalDaoProvider, categoryDaoProvider, milestoneDaoProvider);
  }

  public static GoalRepository newInstance(GoalDao goalDao, CategoryDao categoryDao,
      MilestoneDao milestoneDao) {
    return new GoalRepository(goalDao, categoryDao, milestoneDao);
  }
}
