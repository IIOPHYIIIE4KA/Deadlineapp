package com.alexandr.deadlineapp.Domain

import android.app.Application
import android.provider.SyncStateContract.Helpers.insert
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.alexandr.deadlineapp.R
import com.alexandr.deadlineapp.Repository.Database.AppDatabase
import com.alexandr.deadlineapp.Repository.Database.DAO.DeadlineDAO
import com.alexandr.deadlineapp.Repository.Database.Entity.Deadline
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class AddDeadlineViewModel(app: Application) : AndroidViewModel(app), CoroutineScope {
    private var database: AppDatabase = AppDatabase.getDatabase(app)
    var deadline: Deadline? = null
    var edit: MutableLiveData<Boolean> = MutableLiveData()

    private var deadlinesDao = database.getDeadlinesDAO()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private  val job: Job = Job()


    fun addDeadline(deadline: Deadline) {
        launch(Dispatchers.IO){
            deadlinesDao.insert(deadline)
        }
    }

    fun updateDeadline(deadline: Deadline) {
        launch(Dispatchers.IO){
            deadlinesDao.update(deadline)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}