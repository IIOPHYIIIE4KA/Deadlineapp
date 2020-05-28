package com.alexandr.deadlineapp.Domain

import android.app.Activity
import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
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


class DeadlineViewModel(private val app: Application) :
    AndroidViewModel(app)/*, CoroutineScope*/ {
    var deadlines: MutableLiveData<List<Deadline>> = MutableLiveData()
    var edit : MutableLiveData<Boolean> = MutableLiveData()

    private var deadline : Deadline = Deadline(name = "", date = "", time = "")
    private var database: AppDatabase = AppDatabase.getDatabase(app)
    private val deadlinesRepository = DeadlinesRepository(database)
    //private var deadlinesDAO = database.getDeadlinesDAO()
    //override val coroutineContext: CoroutineContext
    //    get() = Dispatchers.Main + job

    val data = deadlinesRepository.getDeadlines()
    val dataCurrent = deadlinesRepository.getDeadlinesCurrent()
    val foundData =  deadlinesRepository.foundData

    //private val job : Job = Job()

    fun requestInformation() {
        /*launch(Dispatchers.Main) {
            deadlines.value = withContext(Dispatchers.Default) {
                deadlinesDAO.getAll()
            }
        }*/
    }


    fun saveInformation(deadline: Deadline) {
        /*launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
               deadlinesDAO.insert(deadline)
            }
            deadlines.value = withContext(Dispatchers.IO) {
                deadlinesDAO.getAll()
            }
        }*/
        deadlinesRepository.insertDeadline(deadline)
    }

    fun deleteInformation() {
        /*launch (Dispatchers.IO) {
            deadlinesDAO.deleteList(deadlines.value as List<Deadline>)
        }
        requestInformation()*/
        deadlinesRepository.deleteDeadline(deadline)
    }

    fun deleteOne(deadline: Deadline) {
        /*launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                deadlinesDAO.delete(deadline)
            }
            deadlines.value = withContext(Dispatchers.IO) {
                deadlinesDAO.getAll()
            }
        }*/
        deadlinesRepository.deleteDeadline(deadline)
    }

    fun updateOne(deadline: Deadline) {
        /*launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                deadlinesDAO.update(deadline)
            }
            deadlines.value = withContext(Dispatchers.IO) {
                deadlinesDAO.getAll()
            }
        }*/
        deadlinesRepository.updateDeadline(deadline)
        edit.value = false
    }

    fun edit(d: Deadline) {
        this.deadline = d
        edit.value = true
    }

    fun getDeadline() : Deadline {
        return this.deadline
    }

    override fun onCleared() {
        super.onCleared()
        //job.cancel()
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