package com.alexandr.deadlineapp.Presentation.Acrivities.Main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.deadline_fragment.*


class DeadlineFragment : Fragment(), View.OnClickListener {

    private lateinit var mainActivity : MainActivity
    private lateinit var viewModel : DeadlineViewModel

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
    }

    private fun editDeadline() {
        mainActivity.btadd.setOnClickListener(this)
        viewModel.edit.observe(viewLifecycleOwner, Observer<Boolean>{
            if (it) {
                mainActivity.bottomShow()
                mainActivity.btadd.text = resources.getString(R.string.updateButton)
                val item = viewModel.getDeadline()
                mainActivity.editPredmet.setText(item.name)
                mainActivity.editDescription.setText(item.description)
                mainActivity.editDate.setText(item.date)
                mainActivity.editTime.setText(item.time)
                mainActivity.btadd.setOnClickListener {
                    val predmet =  mainActivity.editPredmet.text.toString()
                    val descr = mainActivity.editDescription.text.toString()
                    var color: Int = R.color.colorLow;
                    if (mainActivity.chipMedium.isChecked) {
                        color = R.color.colorMedium
                    }
                    if (mainActivity.chipHigh.isChecked) {
                        color = R.color.colorHigh
                    }
                    val pinned = mainActivity.imgPinned.contentDescription == "pinned"
                    val date = mainActivity.editDate.text.toString()
                    val time = mainActivity.editTime.text.toString()
                    item.name = predmet
                    item.description = descr
                    item.date = date
                    item.time = time
                    item.pinned = pinned
                    item.importance = color
                    viewModel.updateOne(item)
                    Toast.makeText(requireContext(), "Дедлайн обновлен!", Toast.LENGTH_SHORT).show()
                    mainActivity.clear()
                }
            } else {
                mainActivity.btadd.text = resources.getString(R.string.add)
                mainActivity.btadd.setOnClickListener(this)
            }
        })
    }

    fun defaultData() {
        viewModel.data.observe(viewLifecycleOwner, Observer {
            if (recycler.adapter == null) {
                recycler.adapter =
                    MyDeadlineRecyclerViewAdapter(
                        it,
                        requireContext(),
                        viewModel
                    )
            }
        })
    }

    private fun requestData(typeData: DataType) {
        unObserve()
        when(typeData){
            DataType.FULL -> {
                viewModel.data.observe(viewLifecycleOwner, Observer {
                    (recycler.adapter as MyDeadlineRecyclerViewAdapter).updateBookList(it)
                })
            }
            DataType.FIND -> {
                viewModel.foundData.observe(viewLifecycleOwner, Observer {
                    (recycler.adapter as MyDeadlineRecyclerViewAdapter).updateBookList(it)
                })
            }
            DataType.NOTCOMP -> {
                viewModel.dataCurrent.observe(viewLifecycleOwner, Observer {
                    (recycler.adapter as MyDeadlineRecyclerViewAdapter).updateBookList(it)
                })
            }
        }
        type = typeData
    }

    /*private fun requestData(viewModel: DeadlineViewModel, recycler: RecyclerView) {
        viewModel.requestInformation()
        recycler.layoutManager = LinearLayoutManager(context)
        viewModel.deadlines.observe(viewLifecycleOwner, Observer<List<Deadline>>{
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
        })
    }*/

    private fun setRecycler() {
        recycler.layoutManager = LinearLayoutManager(context)
        registerForContextMenu(recycler);
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recycler)
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy >0) {
                    mainActivity.bottomHide()
                }
                else if (dy <0) {
                    mainActivity.bottomHide()
                }
            }
        })
    }

    private var simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
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
        val predmet =  mainActivity.editPredmet.text.toString()
        val descr = mainActivity.editDescription.text.toString()
        var color: Int = R.color.colorLow;
        if (mainActivity.chipMedium.isChecked) {
            color = R.color.colorMedium
        }
        if (mainActivity.chipHigh.isChecked) {
            color = R.color.colorHigh
        }
        val pinned = mainActivity.imgPinned.contentDescription == "pinned"
        val date = mainActivity.editDate.text.toString()
        val time = mainActivity.editTime.text.toString()
        val d = Deadline(name = predmet, description = descr, pinned = pinned,
            date = date, time = time, importance = color)
        viewModel.saveInformation(d)
        Toast.makeText(requireContext(), "Дедлайн добавлен!", Toast.LENGTH_SHORT).show()
        mainActivity.bottomHide()
        mainActivity.clear()
    }

    private fun unObserve() {
        when (type) {
            DataType.FULL -> viewModel.clearObserveData(viewLifecycleOwner)
            DataType.FIND -> viewModel.clearObserveFind(viewLifecycleOwner)
            DataType.NOTCOMP -> viewModel.clearObserveDataCur(viewLifecycleOwner)
        }
    }
}
