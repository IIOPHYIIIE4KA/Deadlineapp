package com.alexandr.deadlineapp

import android.app.Application
import androidx.room.Room
import com.alexandr.deadlineapp.Repository.Database.AppDatabase

class App: Application() {

    private var database: AppDatabase? = null

    override fun onCreate() {
        super.onCreate()
        //initRoom()
    }

    private fun initRoom() {
        database = Room.databaseBuilder(this, AppDatabase::class.java, "database")
            .build()
    }

}