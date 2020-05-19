package com.alexandr.deadlineapp.Domain

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alexandr.deadlineapp.App
import com.alexandr.deadlineapp.Repository.Database.AppDatabase
import com.alexandr.deadlineapp.Repository.Database.DAO.DeadlineDAO
import com.alexandr.deadlineapp.Repository.Database.Entity.Deadline
import com.alexandr.deadlineapp.Repository.DeadlinesRepository
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class DeadlineViewModel(private val app: Application) : AndroidViewModel(app), CoroutineScope {
    var deadlines: MutableLiveData<List<Deadline>> = MutableLiveData()
    private var database: AppDatabase = AppDatabase.getDatabase(app)
    private var deadlinesDAO = database.getDeadlinesDAO()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val job : Job = Job()

    fun requestInformation() {
        launch(Dispatchers.Main) {
            deadlines.value = withContext(Dispatchers.Default) {
                deadlinesDAO.getAll()
            }
        }
    }

    fun saveInformation(deadline: Deadline) {
        launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
               deadlinesDAO.insert(deadline)
            }
            deadlines.value = withContext(Dispatchers.IO) {
                deadlinesDAO.getAll()
            }
        }
    }

    fun deleteInformation() {
        launch (Dispatchers.IO) {
            deadlinesDAO.deleteList(deadlines.value as List<Deadline>)
        }
        requestInformation()
    }

    fun deleteOne(deadline: Deadline) {
        launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                deadlinesDAO.delete(deadline)
            }
            deadlines.value = withContext(Dispatchers.IO) {
                deadlinesDAO.getAll()
            }
        }
    }

    fun updateOne(deadline: Deadline) {
        launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                deadlinesDAO.update(deadline)
            }
            deadlines.value = withContext(Dispatchers.IO) {
                deadlinesDAO.getAll()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel();
    }


}