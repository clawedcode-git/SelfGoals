package com.example.selfgoals.ui.dashboard;

import android.app.Application;
import com.example.selfgoals.data.repository.GoalRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class DashboardViewModel_Factory implements Factory<DashboardViewModel> {
  private final Provider<Application> applicationProvider;

  private final Provider<GoalRepository> repositoryProvider;

  public DashboardViewModel_Factory(Provider<Application> applicationProvider,
      Provider<GoalRepository> repositoryProvider) {
    this.applicationProvider = applicationProvider;
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public DashboardViewModel get() {
    return newInstance(applicationProvider.get(), repositoryProvider.get());
  }

  public static DashboardViewModel_Factory create(Provider<Application> applicationProvider,
      Provider<GoalRepository> repositoryProvider) {
    return new DashboardViewModel_Factory(applicationProvider, repositoryProvider);
  }

  public static DashboardViewModel newInstance(Application application, GoalRepository repository) {
    return new DashboardViewModel(application, repository);
  }
}
