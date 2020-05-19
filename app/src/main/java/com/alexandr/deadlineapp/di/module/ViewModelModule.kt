package com.alexandr.deadlineapp.di.module

import android.app.Application
import com.alexandr.deadlineapp.App
import com.alexandr.deadlineapp.Domain.DeadlineViewModel
import com.alexandr.deadlineapp.di.scope.ViewModelScope
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule(app: App) {

    internal var app: Application = app

    @ViewModelScope
    @Provides
    internal fun providesViewModel(): DeadlineViewModel {
        return DeadlineViewModel(app)
    }

}