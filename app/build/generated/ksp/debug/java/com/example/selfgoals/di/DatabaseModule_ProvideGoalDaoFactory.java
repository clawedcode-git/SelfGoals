package com.example.selfgoals.di;

import com.example.selfgoals.data.SelfGoalsDatabase;
import com.example.selfgoals.data.dao.GoalDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class DatabaseModule_ProvideGoalDaoFactory implements Factory<GoalDao> {
  private final Provider<SelfGoalsDatabase> dbProvider;

  public DatabaseModule_ProvideGoalDaoFactory(Provider<SelfGoalsDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public GoalDao get() {
    return provideGoalDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideGoalDaoFactory create(
      Provider<SelfGoalsDatabase> dbProvider) {
    return new DatabaseModule_ProvideGoalDaoFactory(dbProvider);
  }

  public static GoalDao provideGoalDao(SelfGoalsDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideGoalDao(db));
  }
}
