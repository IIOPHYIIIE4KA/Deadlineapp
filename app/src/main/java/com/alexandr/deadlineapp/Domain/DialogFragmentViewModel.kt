package com.alexandr.deadlineapp.Domain

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.alexandr.deadlineapp.Repository.Database.AppDatabase
import com.alexandr.deadlineapp.Repository.Database.Entity.Deadline
import com.alexandr.deadlineapp.Repository.DeadlinesRepository

class DialogFragmentViewModel(app: Application) :
    AndroidViewModel(app) {
    private var deadline : Deadline = Deadline(name = "", date = "", time = "")
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