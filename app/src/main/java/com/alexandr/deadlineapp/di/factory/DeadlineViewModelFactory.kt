package com.alexandr.deadlineapp.di.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alexandr.deadlineapp.Domain.DeadlinesViewModel
import com.alexandr.deadlineapp.Repository.DeadlinesRepository
import javax.inject.Inject


class DeadlineViewModelFactory @Inject constructor(private var deadlinesRepository: DeadlinesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DeadlinesViewModel(deadlinesRepository) as T
    }
}