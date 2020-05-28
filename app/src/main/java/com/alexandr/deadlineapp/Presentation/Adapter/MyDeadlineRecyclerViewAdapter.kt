package com.alexandr.deadlineapp.Presentation.Adapter


import android.content.Context
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alexandr.deadlineapp.Domain.DeadlineViewModel
import com.alexandr.deadlineapp.Presentation.Item.DeadlinesViewHolder
import com.alexandr.deadlineapp.R
import com.alexandr.deadlineapp.Repository.Database.Entity.Deadline
import com.alexandr.deadlineapp.Utils.DeadlinesDiffCallback


class MyDeadlineRecyclerViewAdapter (private var items : List<Deadline>,
                                     val context : Context,
                                     private val deadlineViewModel : DeadlineViewModel
) :
    RecyclerView.Adapter<DeadlinesViewHolder>() {


    fun updateBookList(newDeadlinesList: List<Deadline>) {
        val diffResult = DiffUtil.calculateDiff(DeadlinesDiffCallback(items, newDeadlinesList), true)
        items = newDeadlinesList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeadlinesViewHolder {
        return DeadlinesViewHolder(LayoutInflater.from(context).inflate(R.layout.deadline_card, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size;
    }

    override fun onBindViewHolder(holder: DeadlinesViewHolder, position: Int) {
        var color: Int = R.color.defaultCardColor
        if (items[position].pinned) {
            color = R.color.colorCompleted
        }
        if (items[position].completed) {
            color = R.color.colorPinned
        }
        val listener = View.OnCreateContextMenuListener { menu, _, _ ->
            if (!items[position].completed) {
                menu.add("Выполнено").setOnMenuItemClickListener {
                    items[position].completed = true
                    deadlineViewModel.updateOne(items[position])
                    true
                }
                if (items[position].pinned) {
                    menu.add("Открепить").setOnMenuItemClickListener {
                        Toast.makeText(context, "Дедлайн откреплен", Toast.LENGTH_SHORT).show()
                        items[position].pinned = false
                        deadlineViewModel.updateOne(items[position])
                        true
                    }
                } else {
                    menu.add("Закрепить").setOnMenuItemClickListener {
                        Toast.makeText(context, "Дедлайн закреплен", Toast.LENGTH_SHORT).show()
                        items[position].pinned = true
                        deadlineViewModel.updateOne(items[position])
                        true
                    }
                }
                menu.add("Редактировать").setOnMenuItemClickListener {
                    deadlineViewModel.edit(items[position])
                    true
                }
            }
            menu.add("Удалить").setOnMenuItemClickListener {
                Toast.makeText(context, "Дедлайн удален", Toast.LENGTH_SHORT).show()
                deadlineViewModel.deleteOne(items[position])
                true
            }
        }
        holder.setDeadline(items[position],ContextCompat.getColor(context,color), listener)
    }

}

