package com.example.selfgoals.ui.dashboard;

import android.app.Application;
import androidx.work.WorkManager;
import com.example.selfgoals.data.repository.GoalRepository;
import com.example.selfgoals.data.repository.SettingsRepository;
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

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  private final Provider<WorkManager> workManagerProvider;

  public DashboardViewModel_Factory(Provider<Application> applicationProvider,
      Provider<GoalRepository> repositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<WorkManager> workManagerProvider) {
    this.applicationProvider = applicationProvider;
    this.repositoryProvider = repositoryProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
    this.workManagerProvider = workManagerProvider;
  }

  @Override
  public DashboardViewModel get() {
    return newInstance(applicationProvider.get(), repositoryProvider.get(), settingsRepositoryProvider.get(), workManagerProvider.get());
  }

  public static DashboardViewModel_Factory create(Provider<Application> applicationProvider,
      Provider<GoalRepository> repositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<WorkManager> workManagerProvider) {
    return new DashboardViewModel_Factory(applicationProvider, repositoryProvider, settingsRepositoryProvider, workManagerProvider);
  }

  public static DashboardViewModel newInstance(Application application, GoalRepository repository,
      SettingsRepository settingsRepository, WorkManager workManager) {
    return new DashboardViewModel(application, repository, settingsRepository, workManager);
  }
}
