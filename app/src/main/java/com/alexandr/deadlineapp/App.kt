package com.alexandr.deadlineapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.room.Room
import com.alexandr.deadlineapp.Repository.Database.AppDatabase
import com.alexandr.deadlineapp.di.component.*
import com.alexandr.deadlineapp.di.module.RoomDatabaseModule

class App: Application() {
    private var database: AppDatabase? = null
    lateinit var deadlineComponent: DeadlineComponent

    override fun onCreate() {
        super.onCreate()
        //initRoom()
        initDagger()
        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_YES);
    }

    private fun initRoom() {
        database = Room.databaseBuilder(this,AppDatabase::class.java, "database")
            .build()
    }

    private fun initDagger() {
        deadlineComponent = DaggerDeadlineComponent
            .builder()
            .roomDatabaseModule(RoomDatabaseModule(this))
            .build()
    }



}