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
    var importance: Int = R.color.colorLow) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Deadline) return false
        val deadline = other as? Deadline
        return false
        /*this.id == deadline?.id
                && this.name == deadline?.name
                && this.description == deadline?.description
                && this.completed == deadline?.completed
                && this.pinned == deadline?.pinned
                && this.date == deadline?.date
                && this.time == deadline?.time
                && this.importance == deadline?.importance*/
    }
}

