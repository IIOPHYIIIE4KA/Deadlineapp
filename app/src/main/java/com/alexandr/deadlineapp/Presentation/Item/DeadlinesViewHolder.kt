package com.alexandr.deadlineapp.Presentation.Item

import android.view.ContextMenu
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.alexandr.deadlineapp.R
import kotlinx.android.synthetic.main.deadline_card.view.*
import kotlinx.android.synthetic.main.dialog_add.view.*

class DeadlinesViewHolder(view : View) : RecyclerView.ViewHolder(view) {
    val predmet = view.tvPred
    val zadanie = view.tvZad
    val completed = view.rbComp
    val pinned = view.imgPin
    val datetime = view.tvDateTime
    val importance : Int? = com.alexandr.deadlineapp.R.color.colorLow
    val card = view.card

}