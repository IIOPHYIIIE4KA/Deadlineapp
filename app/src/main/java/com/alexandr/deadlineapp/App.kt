package com.alexandr.deadlineapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.room.Room
import com.alexandr.deadlineapp.Repository.Database.AppDatabase


class App: Application() {


    override fun onCreate() {
        super.onCreate()
        //initRoom()
        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }





}