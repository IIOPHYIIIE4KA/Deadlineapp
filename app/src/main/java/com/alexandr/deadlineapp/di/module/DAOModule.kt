package com.alexandr.deadlineapp.di.module

import com.alexandr.deadlineapp.Repository.Database.AppDatabase
import com.alexandr.deadlineapp.Repository.Database.DAO.DeadlineDAO
import dagger.Module
import dagger.Provides

@Module
class DAOModule (private val mAppDatabase: AppDatabase) {

    @Provides
    internal fun providesDatabase(): AppDatabase {
        return mAppDatabase
    }

    @Provides
    internal fun providesDAO(database: AppDatabase): DeadlineDAO {
        return database.getDeadlinesDAO()
    }
}