package com.alexandr.deadlineapp.di.module

import android.app.Application
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.alexandr.deadlineapp.Repository.Database.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomDatabaseModule(application: Application) {

    private var application = application
    private lateinit var appDatabase: AppDatabase

    @Singleton
    @Provides
    fun providesRoomDatabase(): AppDatabase {
        appDatabase = Room.databaseBuilder(application, AppDatabase::class.java, "database")
            .build()
        return appDatabase
    }

    @Singleton
    @Provides
    fun providesDeadlineDAO(appDatabase: AppDatabase) = appDatabase.getDeadlinesDAO()

}