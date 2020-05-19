package com.alexandr.deadlineapp.di.component

import com.alexandr.deadlineapp.Presentation.Acrivities.Main.DeadlineFragment
import com.alexandr.deadlineapp.Presentation.Acrivities.Main.MainActivity
import com.alexandr.deadlineapp.di.module.DeadlineModule
import com.alexandr.deadlineapp.di.module.RoomDatabaseModule
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [RoomDatabaseModule::class, DeadlineModule::class])
interface DeadlineComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(fragment: DeadlineFragment)
}