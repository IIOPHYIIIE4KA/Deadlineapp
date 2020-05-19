package com.alexandr.deadlineapp.Utils

import androidx.recyclerview.widget.DiffUtil
import com.alexandr.deadlineapp.Repository.Database.Entity.Deadline

class DeadlinesDiffCallback(private val oldDeadlinesList: List<Deadline>?, private val newDeadlinesList: List<Deadline>?) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldDeadlinesList?.get(oldItemPosition)?.id == newDeadlinesList?.get(newItemPosition)?.id
    }

    override fun getOldListSize(): Int {
        return oldDeadlinesList?.size ?: 0
    }

    override fun getNewListSize(): Int {
        return newDeadlinesList?.size ?: 0
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldDeadlinesList?.get(oldItemPosition)?.equals(newDeadlinesList?.get(newItemPosition))!!
    }
}