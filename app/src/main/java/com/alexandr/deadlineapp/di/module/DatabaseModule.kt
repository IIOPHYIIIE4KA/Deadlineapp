package com.alexandr.deadlineapp.di.module

import android.content.Context
import androidx.room.Room
import com.alexandr.deadlineapp.Repository.Database.AppDatabase
import com.alexandr.deadlineapp.Repository.Database.DAO.DeadlineDAO
import com.alexandr.deadlineapp.di.Qualifier.ApplicationContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DatabaseModule(@ApplicationContext context: Context) {
    @ApplicationContext
    private val mContext: Context = context

    @Singleton
    @Provides
    fun provideDatabase(): AppDatabase {
        return Room.databaseBuilder(
            mContext,
            AppDatabase::class.java,
            "database"
        ).build()
    }


    @Singleton
    @Provides
    fun provideDeadlineDao(db: AppDatabase): DeadlineDAO {
        return db.getDeadlinesDAO()
    }


}