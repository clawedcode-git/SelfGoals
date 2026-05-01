package com.example.selfgoals.di;

import com.example.selfgoals.data.SelfGoalsDatabase;
import com.example.selfgoals.data.dao.MilestoneDao;
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
public final class DatabaseModule_ProvideMilestoneDaoFactory implements Factory<MilestoneDao> {
  private final Provider<SelfGoalsDatabase> dbProvider;

  public DatabaseModule_ProvideMilestoneDaoFactory(Provider<SelfGoalsDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public MilestoneDao get() {
    return provideMilestoneDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideMilestoneDaoFactory create(
      Provider<SelfGoalsDatabase> dbProvider) {
    return new DatabaseModule_ProvideMilestoneDaoFactory(dbProvider);
  }

  public static MilestoneDao provideMilestoneDao(SelfGoalsDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideMilestoneDao(db));
  }
}
