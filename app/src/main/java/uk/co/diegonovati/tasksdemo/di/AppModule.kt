package uk.co.diegonovati.tasksdemo.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uk.co.diegonovati.tasksdemo.data.datasources.*
import uk.co.diegonovati.tasksdemo.data.repositories.ConnectivityMonitorRepository
import uk.co.diegonovati.tasksdemo.data.repositories.TasksLocalRepository
import uk.co.diegonovati.tasksdemo.data.repositories.TasksRemoteRepository
import uk.co.diegonovati.tasksdemo.domain.repositories.IConnectivityMonitorRepository
import uk.co.diegonovati.tasksdemo.domain.repositories.ITasksLocalRepository
import uk.co.diegonovati.tasksdemo.domain.repositories.ITasksRemoteRepository
import uk.co.diegonovati.tasksdemo.domain.usecases.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideProviderRetrofit(): ProviderRetrofit = ProviderRetrofit()

    @Singleton
    @Provides
    fun provideProviderDatabase(@ApplicationContext appContext: Context): TasksDemoDatabase = ProviderDatabase.createDatabase(appContext)

    @Singleton
    @Provides
    fun provideRemoteDataSource(providerRetrofit: ProviderRetrofit): IRemoteDataSource = RemoteDataSource(providerRetrofit.getRetrofit())

    // Persistence of data using SharedPreferences
//    @Singleton
//    @Provides
//    fun provideLocalDataSourceSharedPreferences(@ApplicationContext appContext: Context): ILocalDataSource = LocalDataSourceSharedPreferences(appContext)

    // Persistence of data using Room
    @Singleton
    @Provides
    fun provideLocalDataSourceRoom(tasksDemoDatabase: TasksDemoDatabase): ILocalDataSource = LocalDataSourceRoom(tasksDemoDatabase)

    @Singleton
    @Provides
    fun provideConnectivityMonitorDataSource(@ApplicationContext appContext: Context): IConnectivityMonitorDataSource = ConnectivityMonitorDataSource(appContext)

    @Singleton
    @Provides
    fun provideTasksRemoteRepository(remoteDataSource: IRemoteDataSource): ITasksRemoteRepository = TasksRemoteRepository(remoteDataSource)

    @Singleton
    @Provides
    fun provideTasksLocalRepository(localDataSource: ILocalDataSource): ITasksLocalRepository = TasksLocalRepository(localDataSource)

    @Singleton
    @Provides
    fun provideConnectivityMonitorRepository(connectivityMonitorDataSource: IConnectivityMonitorDataSource): IConnectivityMonitorRepository = ConnectivityMonitorRepository(connectivityMonitorDataSource)

    @Singleton
    @Provides
    fun provideUseCaseTaskList(tasksLocalRepository: ITasksLocalRepository, tasksRemoteRepository: ITasksRemoteRepository): UseCaseTaskList = UseCaseTaskList(tasksLocalRepository, tasksRemoteRepository)

    @Singleton
    @Provides
    fun provideUseCaseTaskFilter(tasksLocalRepository: ITasksLocalRepository): UseCaseTaskFilter = UseCaseTaskFilter(tasksLocalRepository)

    @Singleton
    @Provides
    fun provideUseCaseConnectivityMonitorStart(connectivityMonitorRepository: IConnectivityMonitorRepository): UseCaseConnectivityMonitorStart = UseCaseConnectivityMonitorStart(connectivityMonitorRepository)

    @Singleton
    @Provides
    fun provideUseCaseConnectivityMonitorStop(connectivityMonitorRepository: IConnectivityMonitorRepository): UseCaseConnectivityMonitorStop = UseCaseConnectivityMonitorStop(connectivityMonitorRepository)

    @Singleton
    @Provides
    fun provideUseCaseConnectivitySetChangeListener(connectivityMonitorRepository: IConnectivityMonitorRepository): UseCaseConnectivitySetChangeListener = UseCaseConnectivitySetChangeListener(connectivityMonitorRepository)
}