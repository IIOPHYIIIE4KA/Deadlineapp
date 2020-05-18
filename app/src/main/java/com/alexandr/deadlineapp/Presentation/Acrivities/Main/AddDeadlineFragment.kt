package com.alexandr.deadlineapp.Presentation.Acrivities.Main

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.alexandr.deadlineapp.Domain.AddDeadlineViewModel
import com.alexandr.deadlineapp.R
import com.alexandr.deadlineapp.Utils.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_add.*
import java.util.*

class AddDeadlineFragment : Fragment() {

    private lateinit var mainActivity: MainActivity
    companion object {
        fun newInstance() =
            AddDeadlineFragment()
    }

    private lateinit var viewModel: AddDeadlineViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_add, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainActivity = activity as MainActivity
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        //activity?.toolbar?.menu?.findItem(R.id.menuAdd)?.isVisible = true
        //activity?.toolbar?.menu?.findItem(R.id.menuClr)?.isVisible = false
        activity?.fab?.isVisible = false
        viewModel = ViewModelProviders.of(mainActivity).get(AddDeadlineViewModel::class.java)
        (activity as AppCompatActivity).supportActionBar?.title = "Добавить"
        imageButtonDate.setOnClickListener {
            DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val c = Calendar.getInstance()
                c.set(year,monthOfYear,dayOfMonth)
                var date: String = "${Utils.WeekDay(c.get(Calendar.DAY_OF_WEEK))}, ${year}."
                val dayOfWeek = c[Calendar.DAY_OF_WEEK]
                if (monthOfYear<10){
                    date += "0${monthOfYear}."
                } else { date += "${monthOfYear}."   }
                if (dayOfMonth<10){
                    date += "0${dayOfMonth}"
                } else { date += "${dayOfMonth}"   }
                editDate.setText(date)
            }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH) ).show()
        }
        imageButtonTime.setOnClickListener {
            TimePickerDialog(requireContext(), TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                var time: String
                if (hourOfDay<10){
                    time = "0${hourOfDay}:"
                } else {   time = "${hourOfDay}:"  }
                if (minute<10){
                    time += "0${minute}"
                } else {   time += "${minute}"  }
                editTime.setText(time)
            }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE),true).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.fab?.isVisible = true
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(false)
        //activity?.toolbar?.menu?.findItem(R.id.menuAdd)?.isVisible = false
        //activity?.toolbar?.menu?.findItem(R.id.menuClr)?.isVisible = true
        (activity as AppCompatActivity).supportActionBar?.title = "Дедлайны"
    }
}