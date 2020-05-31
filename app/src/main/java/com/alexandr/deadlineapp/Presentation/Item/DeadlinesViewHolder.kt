package com.alexandr.deadlineapp.Presentation.Item

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.alexandr.deadlineapp.R
import com.alexandr.deadlineapp.Repository.Database.Entity.Deadline
import kotlinx.android.synthetic.main.deadline_card.view.*
import java.util.*

class DeadlinesViewHolder(view : View) : RecyclerView.ViewHolder(view), View.OnClickListener {
    val predmet = view.tvPred
    val zadanie = view.tvZad
    val completed = view.rbComp
    val pinned = view.imgPin
    val datetime = view.tvDateTime
    val importance : Int? = R.color.colorLow
    val card = view.card
    val img = view.imgClock
    private lateinit var deadline: Deadline

    fun setDeadline(deadline: Deadline, color: Int, onCreateContextMenuListener: View.OnCreateContextMenuListener){
        this.deadline = deadline
        predmet.text = deadline.name.toUpperCase(Locale.ROOT)
        zadanie.text = deadline.description
        completed.isChecked = deadline.completed
        if (deadline.pinned){
            pinned.visibility = View.VISIBLE
        }
        else  { pinned.visibility = View.INVISIBLE }
        if (deadline.date == "") {
             datetime.text = if (deadline.time == "") "" else deadline.time
        } else {
             datetime.text = if (deadline.time == "") deadline.date else "${deadline.date}, ${deadline.time}"
        }
        when (deadline.importance) {
            R.color.colorLow -> {
                img.setImageResource(R.drawable.ic_access_alarm_low_24dp)
            }
            R.color.colorMedium -> {
                img.setImageResource(R.drawable.ic_access_alarm_medium_24dp)
            }
            R.color.colorHigh -> {
                img.setImageResource(R.drawable.ic_access_alarm_high_24dp)
            }
        }
        completed.setOnClickListener(this)
        card.setCardBackgroundColor(color)
        card.setOnCreateContextMenuListener(onCreateContextMenuListener)
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.rbComp)
        {(v as RadioButton).isChecked = deadline.completed}
    }

    fun getDeadline(): Deadline {
        return deadline
    }

}