package com.alexandr.deadlineapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.room.Room
import com.alexandr.deadlineapp.Repository.Database.AppDatabase
import com.alexandr.deadlineapp.di.component.*
import com.alexandr.deadlineapp.di.module.RoomDatabaseModule

class App: Application() {

    lateinit var deadlineComponent : DeadlineComponent

    val s: String = "12344"
    override fun onCreate() {
        super.onCreate()
        //initRoom()
        initDagger()
        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }

    private fun initDagger() {
        deadlineComponent = DaggerDeadlineComponent
            .builder()
            .roomDatabaseModule(RoomDatabaseModule(this))
            .build()
    }



}