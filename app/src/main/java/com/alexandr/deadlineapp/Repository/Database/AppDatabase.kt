package com.alexandr.deadlineapp.Repository.Database


import android.content.Context
import androidx.room.Database
import androidx.room.Room

import androidx.room.RoomDatabase
import com.alexandr.deadlineapp.Repository.Database.DAO.DeadlineDAO
import com.alexandr.deadlineapp.Repository.Database.Entity.Deadline

@Database(entities = [Deadline::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun getDeadlinesDAO() : DeadlineDAO
    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,AppDatabase::class.java, "database")
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}