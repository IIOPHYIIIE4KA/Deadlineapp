package com.alexandr.deadlineapp

import android.app.Application
import android.widget.Toast
import androidx.room.Room
import com.alexandr.deadlineapp.Repository.Database.AppDatabase
import com.alexandr.deadlineapp.di.component.*
import com.alexandr.deadlineapp.di.module.ApplicationModule
import com.alexandr.deadlineapp.di.module.ViewModelModule
import com.alexandr.deadlineapp.di.module.DAOModule
import com.alexandr.deadlineapp.di.module.DatabaseModule

class App: Application() {
    private lateinit var mApplicationComponent: ApplicationComponent
    private var database: AppDatabase? = null
    private var viewModelComponent: ViewModelComponent? = null
    private var DAOComponent: DAOComponent? = null

    override fun onCreate() {
        super.onCreate()
        initRoom()
        initDagger()
    }

    private fun initRoom() {
        database = Room.databaseBuilder(this,AppDatabase::class.java, "database")
            .build()
    }

    private fun initDagger() {
        val daoComponent = DaggerDAOComponent.builder()
            .dAOModule(DAOModule(this.database!!))
            .build()


        viewModelComponent = DaggerViewModelComponent.builder()
            .viewModelModule(ViewModelModule(this))
            .build()

        mApplicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .databaseModule(DatabaseModule(this))
            .build()
        mApplicationComponent.inject(this);
    }

    fun getViewModelComponent(): ViewModelComponent {
        return this.viewModelComponent!!
    }

    fun getDAOComponent(): DAOComponent {
        return this.DAOComponent!!
    }

    fun getAppComponent(): ApplicationComponent{
        return mApplicationComponent
    }

}