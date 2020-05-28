package com.alexandr.deadlineapp.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alexandr.deadlineapp.Repository.Database.AppDatabase
import com.alexandr.deadlineapp.Repository.Database.DAO.DeadlineDAO
import com.alexandr.deadlineapp.Repository.Database.Entity.Deadline
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DeadlinesRepository(appDatabase: AppDatabase): CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val job : Job = Job()

    private var deadlineDAO = appDatabase.getDeadlinesDAO()

    fun getDeadlines(): LiveData<List<Deadline>> {
        return deadlineDAO.getAllLive()
    }

    fun getDeadlinesCurrent(): LiveData<List<Deadline>> {
        return deadlineDAO.getAllCurrentLive()
    }

    fun insertDeadline(deadline: Deadline) {
        launch(Dispatchers.IO) {
            deadlineDAO.insert(deadline)
        }
    }

    fun deleteDeadline(deadline: Deadline) {
        launch(Dispatchers.IO) {
            deadlineDAO.delete(deadline)
        }
    }

    fun updateDeadline(deadline: Deadline) {
        launch(Dispatchers.IO) {
            deadlineDAO.update(deadline)
        }
    }

    fun cancel() {
        job.cancel()
    }
}