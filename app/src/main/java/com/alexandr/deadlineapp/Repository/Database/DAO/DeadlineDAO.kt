package com.alexandr.deadlineapp.Repository.Database.DAO

import androidx.room.*
import com.alexandr.deadlineapp.Repository.Database.Entity.Deadline

@Dao
interface DeadlineDAO {
    @Query("Select * from deadlines where (pinned = 1) " +
            "union all Select * from deadlines where (pinned = 0 and  completed = 0) " +
            "union all select * from deadlines where (pinned = 0 and completed = 1)")
    fun getAll(): List<Deadline>

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