package com.alexandr.deadlineapp.di.module

import com.alexandr.deadlineapp.Repository.Database.AppDatabase
import com.alexandr.deadlineapp.Repository.DeadlinesRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DeadlineModule {

    @Singleton
    @Provides
    fun providesDeadlinesRepository(appDatabase: AppDatabase): DeadlinesRepository {
        return DeadlinesRepository(appDatabase)
    }

}