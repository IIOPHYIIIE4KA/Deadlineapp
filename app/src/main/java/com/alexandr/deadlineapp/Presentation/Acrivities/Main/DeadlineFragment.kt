package com.alexandr.deadlineapp.Presentation.Acrivities.Main

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexandr.deadlineapp.Domain.DeadlineViewModel
import com.alexandr.deadlineapp.Presentation.Adapter.MyDeadlineRecyclerViewAdapter
import com.alexandr.deadlineapp.R
import com.alexandr.deadlineapp.Repository.Database.Entity.Deadline
import com.alexandr.deadlineapp.Utils.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.deadline_fragment.*
import java.util.*
import kotlin.collections.ArrayList


class DeadlineFragment : Fragment() {

    companion object {
        fun newInstance() =
            DeadlineFragment()
    }

    private lateinit var viewModel: DeadlineViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.deadline_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DeadlineViewModel::class.java)
        viewModel.requestInformation()
        recycler.layoutManager = LinearLayoutManager(context)
        viewModel.deadlines.observe(viewLifecycleOwner, Observer<List<Deadline>>{
            recycler.adapter =
                MyDeadlineRecyclerViewAdapter(
                    ArrayList(it),
                    requireContext(),
                    viewModel
                )
        })
        registerForContextMenu(recycler);

        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy >0) {
                    (activity as MainActivity?)?.bottomHide()
                }
                else if (dy <0) {
                    (activity as MainActivity?)?.bottomHide()
                }
            }
        })
        (activity as AppCompatActivity).toolbar?.menu?.add("Очистить базу данных")?.setOnMenuItemClickListener{
            Toast.makeText(requireContext(), "База данных очищена", Toast.LENGTH_LONG).show()
            viewModel.deleteInformation()
            true
        }
        activity?.imgPinned?.setOnClickListener {
            if (activity?.imgPinned?.contentDescription == "pinned"){
                activity?.imgPinned?.setImageResource(R.drawable.ic_unpin)
                activity?.imgPinned?.contentDescription = "unpinned"
            } else {
                activity?.imgPinned?.setImageResource(R.drawable.ic_pin)
                activity?.imgPinned?.contentDescription = "pinned"
            }
            true
        }
        activity?.btadd?.setOnClickListener {
            val predmet =  activity?.editPredmet?.text.toString()
            val descr = activity?.editDescription?.text.toString()
            var color: Int = R.color.colorLow;
            if (activity?.chipMedium?.isChecked?: false){
                color = R.color.colorMedium
            }
            if (activity?.chipHigh?.isChecked?: false){
                color = R.color.colorHigh
            }
            val pinned = activity?.imgPinned?.contentDescription == "pinned"
            val date = activity?.editDate?.text.toString()
            val time = activity?.editTime?.text.toString()
            val d = Deadline(name = predmet, description = descr, pinned = pinned,
                date = date, time = time, importance = color)
            viewModel.saveInformation(d)
            (activity as MainActivity?)?.clear()
            Toast.makeText(requireContext(), "Дедлайн добавлен!", Toast.LENGTH_SHORT).show()
            (activity as MainActivity?)?.bottomHide()
        }
        activity?.imgCal?.setOnClickListener {
            DatePickerDialog(requireContext(),
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val c = Calendar.getInstance()
                c.set(year,monthOfYear,dayOfMonth)
                var date: String = "${Utils.WeekDay(c.get(Calendar.DAY_OF_WEEK))}, ${year}."
                    date += if (monthOfYear<10){
                        "0${monthOfYear}."
                    } else {
                        "${monthOfYear}."
                    }
                    date += if (dayOfMonth<10){
                        "0${dayOfMonth}"
                    } else {
                        "$dayOfMonth"
                    }
                    activity?.editDate?.setText(date)
            }, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show()
        }
        activity?.imgClk?.setOnClickListener {
            TimePickerDialog(requireContext(),
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                var time: String = if (hourOfDay<10){
                        "0${hourOfDay}:"
                    } else {
                        "${hourOfDay}:"
                    }
                    time += if (minute<10){
                        "0${minute}"
                    } else {
                        "$minute"
                    }
                    activity?.editTime?.setText(time)
            }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),true).show()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}
