package com.alexandr.deadlineapp.Presentation.Acrivities.Main

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexandr.deadlineapp.Domain.DeadlineViewModel
import com.alexandr.deadlineapp.Presentation.Adapter.MyDeadlineRecyclerViewAdapter
import com.alexandr.deadlineapp.Presentation.Item.DeadlinesViewHolder
import com.alexandr.deadlineapp.R
import com.alexandr.deadlineapp.Repository.Database.Entity.Deadline
import com.alexandr.deadlineapp.Utils.Utils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.deadline_fragment.*
import java.util.*


class DeadlineFragment : Fragment(), View.OnClickListener {

    private lateinit var mainActivity : MainActivity
    private lateinit var viewModel : DeadlineViewModel
    private lateinit var bottomSheetBehavior : BottomSheetBehavior<LinearLayout>

    enum class DataType {
        FULL, NOTCOMP, FIND
    }

    var type = DataType.FULL
    companion object {
        fun newInstance() =
            DeadlineFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.deadline_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DeadlineViewModel::class.java)
        mainActivity = activity as MainActivity
        setRecycler()
        defaultData()
        editDeadline()
        setToolbar()
        setBottom()
        fab.setOnClickListener {
            bottomShow()
        }
    }

    private fun editDeadline() {
        btadd.setOnClickListener(this)
        viewModel.edit.observe(viewLifecycleOwner, Observer {
            if (it) {
                bottomShow()
                btadd.text = resources.getString(R.string.updateButton)
                val item = viewModel.getDeadline()
                editPredmet.setText(item.name)
                editDescription.setText(item.description)
                editDate.setText(item.date)
                editTime.setText(item.time)
                btadd.setOnClickListener {
                    val predmet =  editPredmet.text.toString()
                    val descr = editDescription.text.toString()
                    var color: Int = R.color.colorLow
                    if (chipMedium.isChecked) {
                        color = R.color.colorMedium
                    }
                    if (chipHigh.isChecked) {
                        color = R.color.colorHigh
                    }
                    val pinned = imgPinned.contentDescription == "pinned"
                    val date = editDate.text.toString()
                    val time = editTime.text.toString()
                    item.name = predmet
                    item.description = descr
                    item.date = date
                    item.time = time
                    item.pinned = pinned
                    item.importance = color
                    viewModel.updateOne(item)
                    Toast.makeText(requireContext(), "Дедлайн обновлен!", Toast.LENGTH_SHORT).show()
                    clear()
                }
            } else {
                btadd.text = resources.getString(R.string.add)
                btadd.setOnClickListener(this)
            }
        })
    }

    private fun defaultData() {
        viewModel.data.observe(viewLifecycleOwner, Observer {
            if (recycler.adapter == null) {
                recycler.adapter =
                    MyDeadlineRecyclerViewAdapter(
                        it,
                        requireContext(),
                        viewModel
                    )
            } else {
                (recycler.adapter as MyDeadlineRecyclerViewAdapter).updateBookList(it)
            }
            noDeadlines((recycler.adapter as MyDeadlineRecyclerViewAdapter).itemCount != 0)
        })
    }

    private fun requestData(typeData: DataType) {
        unObserve()
        when(typeData){
            DataType.FULL -> {
                viewModel.data.observe(viewLifecycleOwner, Observer {
                    (recycler.adapter as MyDeadlineRecyclerViewAdapter).updateBookList(it)
                    noDeadlines((recycler.adapter as MyDeadlineRecyclerViewAdapter).itemCount != 0)
                })
            }
            DataType.FIND -> {
                viewModel.foundData.observe(viewLifecycleOwner, Observer {
                    (recycler.adapter as MyDeadlineRecyclerViewAdapter).updateBookList(it)
                    noDeadlines((recycler.adapter as MyDeadlineRecyclerViewAdapter).itemCount != 0)
                })
            }
            DataType.NOTCOMP -> {
                viewModel.dataCurrent.observe(viewLifecycleOwner, Observer {
                    (recycler.adapter as MyDeadlineRecyclerViewAdapter).updateBookList(it)
                    noDeadlines((recycler.adapter as MyDeadlineRecyclerViewAdapter).itemCount != 0)
                })
            }
        }
        type = typeData
    }


    private fun setRecycler() {
        recycler.layoutManager = LinearLayoutManager(context)
        registerForContextMenu(recycler)
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recycler)
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy >0) {
                    bottomHide()
                }
                else if (dy <0) {
                    bottomHide()
                }
            }
        })
    }

    private fun noDeadlines(t: Boolean) {
        textViewEmpty.visibility = if (t) View.GONE else View.VISIBLE
    }

    private var simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        override fun onMove(recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
            if (swipeDir == ItemTouchHelper.LEFT) {
                if (viewHolder is DeadlinesViewHolder) {
                    if (viewHolder.completed.isChecked) {
                        viewModel.deleteOne(viewHolder.getDeadline())
                    } else {
                        val d = viewHolder.getDeadline()
                        d.completed = true
                        viewModel.updateOne(d)
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        val predmet =  editPredmet.text.toString()
        val descr = editDescription.text.toString()
        if (descr.isEmpty()) {
            Toast.makeText(requireContext(), R.string.predmetError, Toast.LENGTH_SHORT).show()
            return
        }
        var color: Int = R.color.colorLow
        if (chipMedium.isChecked) {
            color = R.color.colorMedium
        }
        if (chipHigh.isChecked) {
            color = R.color.colorHigh
        }
        val pinned = imgPinned.contentDescription == "pinned"
        val date = editDate.text.toString()
        val time = editTime.text.toString()
        val d = Deadline(name = predmet, description = descr, pinned = pinned,
            date = date, time = time, importance = color)
        viewModel.saveInformation(d)
        //Toast.makeText(requireContext(), "Дедлайн добавлен!", Toast.LENGTH_SHORT).show()
        bottomHide()
        clear()
    }

    private fun unObserve() {
        when (type) {
            DataType.FULL -> viewModel.clearObserveData(viewLifecycleOwner)
            DataType.FIND -> viewModel.clearObserveFind(viewLifecycleOwner)
            DataType.NOTCOMP -> viewModel.clearObserveDataCur(viewLifecycleOwner)
        }
    }

    private fun setToolbar(){
        mainActivity.setSupportActionBar(toolbar)
        mainActivity.toolbar.title = resources.getString(R.string.title)
        mainActivity.toolbar.menu.add("White").setOnMenuItemClickListener {
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO)
            true
        }
        mainActivity.toolbar.menu.add("Black").setOnMenuItemClickListener {
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES)
            true
        }
        mainActivity.toolbar.menu.add("Скрыть/показать выполненные").setOnMenuItemClickListener {
            if (type == DataType.FULL) {
                requestData(DataType.NOTCOMP)
            } else {
                requestData(DataType.FULL)
            }
            true
        }
    }

    private fun setBottom(){
        val datePickerDialog = DatePickerDialog(requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val c = Calendar.getInstance()
                c.set(year,monthOfYear,dayOfMonth)
                var date = "${Utils.WeekDay(c.get(Calendar.DAY_OF_WEEK))}, "
                date += if (dayOfMonth<10) "0${dayOfMonth}." else "${dayOfMonth}."
                date += if (monthOfYear<10) "0${monthOfYear}." else "${monthOfYear}."
                date += "$year"
                editDate.setText(date)
            }, Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
        val timePickerDialog = TimePickerDialog(requireContext(),
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                var time: String = if (hourOfDay<10) "0${hourOfDay}:" else "${hourOfDay}:"
                time += if (minute<10) "0${minute}" else "$minute"
                editTime.setText(time)
            }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
            Calendar.getInstance().get(Calendar.MINUTE),true)
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_layout)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)

        imgCal.setOnClickListener {
            datePickerDialog.show()
        }

        editDate.setOnClickListener {
            datePickerDialog.show()
        }

        editTime.setOnClickListener {
            timePickerDialog.show()
        }

        imgClk.setOnClickListener {
            timePickerDialog.show()
        }

        imgPinned.setOnClickListener { it as ImageView
            if (it.contentDescription == "pinned"){
                it.setImageResource(R.drawable.ic_push_unpin_24px)
                it.contentDescription = "unpinned"
            } else {
                it.setImageResource(R.drawable.ic_push_pin_24px)
                it.contentDescription = "pinned"
            }
        }
        btadd.setOnClickListener(this)
        chipGr.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chipLow -> {  }
                R.id.chipMedium -> {  }
                R.id.chipHigh -> {  }
                else -> chipLow.isChecked = true
            }
        }
    }

    private var bottomSheetCallback: BottomSheetBehavior.BottomSheetCallback = object :
        BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            when (newState) {
                BottomSheetBehavior.STATE_HIDDEN -> {

                }
                BottomSheetBehavior.STATE_EXPANDED -> {

                }
                BottomSheetBehavior.STATE_COLLAPSED -> {
                    val imm = getSystemService(requireContext(), InputMethodManager::class.java)
                    imm?.hideSoftInputFromWindow(bottom_sheet_layout.windowToken, 0)
                }
                BottomSheetBehavior.STATE_DRAGGING -> {

                }
                BottomSheetBehavior.STATE_SETTLING -> {

                }
                BottomSheetBehavior.STATE_HALF_EXPANDED -> {

                }
            }
        }
        override fun onSlide(
            bottomSheet: View,
            slideOffset: Float
        ) {
            fab.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start()
        }
    }

    private fun clear(){
        editDate.text.clear()
        editTime.text.clear()
        editDescription.text.clear()
        editPredmet.text.clear()
        chipLow.isChecked = true
        imgPinned.setImageResource(R.drawable.ic_push_unpin_24px)
        imgPinned.contentDescription = "unpinned"
    }

    private fun bottomShow(){
        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED)
        {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun bottomHide(){
        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN)
        {
            val imm = getSystemService(requireContext(), InputMethodManager::class.java)
            imm?.hideSoftInputFromWindow(bottom_sheet_layout.windowToken, 0)
            Handler().postDelayed({ bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN }, 50)
        }
    }

}
