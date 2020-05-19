package com.alexandr.deadlineapp.di.component

import android.app.Application
import android.content.Context
import com.alexandr.deadlineapp.App
import com.alexandr.deadlineapp.Presentation.Acrivities.Main.MainActivity
import com.alexandr.deadlineapp.di.Qualifier.ApplicationContext
import com.alexandr.deadlineapp.di.module.ApplicationModule
import com.alexandr.deadlineapp.di.module.DatabaseModule
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [ApplicationModule::class, DatabaseModule::class])
interface ApplicationComponent {
    fun inject(Application: App?)
    fun inject(mainActivity: MainActivity?)

    @get:ApplicationContext
    val context: Context?
    val application: Application?

}