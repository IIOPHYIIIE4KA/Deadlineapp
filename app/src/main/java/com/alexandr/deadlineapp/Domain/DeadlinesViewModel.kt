package com.alexandr.deadlineapp.Domain

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alexandr.deadlineapp.Repository.Database.AppDatabase
import com.alexandr.deadlineapp.Repository.Database.Entity.Deadline
import com.alexandr.deadlineapp.Repository.DeadlinesRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class DeadlinesViewModel(private val deadlinesRepository: DeadlinesRepository)
    : ViewModel() {
    var deadlines: LiveData<List<Deadline>> = MutableLiveData()


    fun getInformation(): LiveData<List<Deadline>> {
        return deadlinesRepository.getDeadlines()
    }

    fun deleteDeadline(deadline: Deadline) {
        deadlinesRepository.deleteDeadline(deadline)
    }

}