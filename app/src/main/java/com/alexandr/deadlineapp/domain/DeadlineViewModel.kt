package com.alexandr.deadlineapp.domain

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.alexandr.deadlineapp.repository.database.AppDatabase
import com.alexandr.deadlineapp.repository.database.entity.Deadline
import com.alexandr.deadlineapp.repository.DeadlinesRepository


class DeadlineViewModel(app: Application) :
    AndroidViewModel(app) {
    var edit : MutableLiveData<Deadline> = MutableLiveData()
    var delete : MutableLiveData<Deadline> = MutableLiveData()

    private var database: AppDatabase = AppDatabase.getDatabase(app)
    private val deadlinesRepository = DeadlinesRepository(database)

    val data = deadlinesRepository.getDeadlines()
    val dataCurrent = deadlinesRepository.getDeadlinesCurrent()
    val foundData =  deadlinesRepository.foundData

    fun saveInformation(deadline: Deadline) {
        deadlinesRepository.insertDeadline(deadline)
    }

    fun deleteOne(deadline: Deadline) {
        deadlinesRepository.deleteDeadline(deadline)
    }

    fun setCompleted(deadline: Deadline) {
        deadline.completed = !deadline.completed
        deadlinesRepository.updateDeadline(deadline)
    }

    fun setPinned(deadline: Deadline) {
        deadline.pinned = !deadline.pinned
        deadlinesRepository.updateDeadline(deadline)
    }

    fun edit(d: Deadline) {
        edit.value = d
    }

    fun delete(d: Deadline) {
        delete.value = d
    }

    override fun onCleared() {
        super.onCleared()
        deadlinesRepository.cancel()
    }

    fun find(name: String) {
        deadlinesRepository.findItem(name)
    }

    fun clearObserveData(a: LifecycleOwner) {
        data.removeObservers(a)
    }

    fun clearObserveDataCur(a: LifecycleOwner) {
        dataCurrent.removeObservers(a)
    }

    fun clearObserveFind(a: LifecycleOwner) {
        foundData.removeObservers(a)
    }
}