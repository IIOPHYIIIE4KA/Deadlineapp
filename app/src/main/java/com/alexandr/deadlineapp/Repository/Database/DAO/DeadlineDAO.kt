package com.alexandr.deadlineapp.Repository.Database.DAO

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.alexandr.deadlineapp.Repository.Database.Entity.Deadline

@Dao
interface DeadlineDAO {
    @Query("Select * from deadlines where (pinned = 1) " +
            "union all Select * from deadlines where (pinned = 0 and  completed = 0) " +
            "union all select * from deadlines where (pinned = 0 and completed = 1)")
    fun getAll(): List<Deadline>

    @Query("Select * from deadlines where (pinned = 1) " +
            "union all Select * from deadlines where (pinned = 0 and  completed = 0) " +
            "union all select * from deadlines where (pinned = 0 and completed = 1)")
    fun getAllLive(): LiveData<List<Deadline>>

    @Query("Select * from deadlines where (pinned = 1 and completed = 0) " +
            "union all Select * from deadlines where (pinned = 0 and  completed = 0)")
    fun getAllCurrentLive(): LiveData<List<Deadline>>

    @Insert
    fun insert(deadline: Deadline)

    @Update
    fun update(deadline: Deadline): Int

    @Update
    fun updateList(deadline: List<Deadline>)

    @Delete
    fun delete(deadline: Deadline): Int

    @Delete
    fun deleteList(deadline: List<Deadline>)

}