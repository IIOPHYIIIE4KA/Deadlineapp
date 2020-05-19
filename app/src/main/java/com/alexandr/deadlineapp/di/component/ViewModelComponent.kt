package com.alexandr.deadlineapp.di.component

import com.alexandr.deadlineapp.Presentation.Acrivities.Main.DeadlineFragment
import com.alexandr.deadlineapp.Presentation.Acrivities.Main.MainActivity
import com.alexandr.deadlineapp.di.module.ViewModelModule
import com.alexandr.deadlineapp.di.scope.ViewModelScope
import dagger.Component

@ViewModelScope
@Component(modules = [ViewModelModule::class])
interface ViewModelComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: DeadlineFragment)
}