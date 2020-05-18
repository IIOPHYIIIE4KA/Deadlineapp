package com.alexandr.deadlineapp.Repository.Database.Entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alexandr.deadlineapp.R

@Entity(tableName = "deadlines")
class Deadline(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var name: String,
    var description: String? = null,
    var completed: Boolean = false,
    var pinned: Boolean = false,
    var date: String,
    var time:String,
    var importance: Int = R.color.colorLow)