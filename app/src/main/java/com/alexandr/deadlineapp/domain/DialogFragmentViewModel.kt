package com.alexandr.deadlineapp.domain

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.alexandr.deadlineapp.repository.database.AppDatabase
import com.alexandr.deadlineapp.repository.database.entity.Deadline
import com.alexandr.deadlineapp.repository.DeadlinesRepository

class DialogFragmentViewModel(app: Application) :
    AndroidViewModel(app) {

    private var database: AppDatabase = AppDatabase.getDatabase(app)
    private val deadlinesRepository = DeadlinesRepository(database)

    fun saveInformation(deadline: Deadline) {
        deadlinesRepository.insertDeadline(deadline)
    }

    fun updateOne(deadline: Deadline) {
        deadlinesRepository.updateDeadline(deadline)
    }

    override fun onCleared() {
        super.onCleared()
        deadlinesRepository.cancel()
    }

}