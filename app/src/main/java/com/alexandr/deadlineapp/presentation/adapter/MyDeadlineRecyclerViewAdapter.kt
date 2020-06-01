package com.alexandr.deadlineapp.presentation.adapter


import android.content.Context
import android.view.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alexandr.deadlineapp.domain.DeadlineViewModel
import com.alexandr.deadlineapp.presentation.item.DeadlinesViewHolder
import com.alexandr.deadlineapp.R
import com.alexandr.deadlineapp.repository.database.entity.Deadline
import com.alexandr.deadlineapp.utils.DeadlinesDiffCallback


class MyDeadlineRecyclerViewAdapter (private var items : List<Deadline>,
                                     private val context : Context,
                                     private val deadlineViewModel : DeadlineViewModel
) :
    RecyclerView.Adapter<DeadlinesViewHolder>() {


    fun updateBookList(newDeadlinesList: List<Deadline>) {
        val diffResult =
            DiffUtil.calculateDiff(DeadlinesDiffCallback(items, newDeadlinesList), true)
        items = newDeadlinesList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeadlinesViewHolder {
        return DeadlinesViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.deadline_card, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
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
                    deadlineViewModel.setCompleted(items[position])
                    true
                }
                if (items[position].pinned) {
                    menu.add("Открепить").setOnMenuItemClickListener {
                        deadlineViewModel.setPinned(items[position])
                        true
                    }
                } else {
                    menu.add("Закрепить").setOnMenuItemClickListener {
                        deadlineViewModel.setPinned(items[position])
                        true
                    }
                }
                menu.add("Редактировать").setOnMenuItemClickListener {
                    deadlineViewModel.edit(items[position])
                    true
                }
            }
            menu.add("Удалить").setOnMenuItemClickListener {
                deadlineViewModel.delete(items[position])
                true
            }
            holder.setcontextMenu(menu)
        }
        val click = View.OnClickListener {
            deadlineViewModel.setCompleted(items[position])
        }
        holder.setDeadline(items[position],
            ContextCompat.getColor(context, color), listener, click)
    }

}

