package com.alexandr.deadlineapp.Presentation.activities.main

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexandr.deadlineapp.Domain.DeadlineViewModel
import com.alexandr.deadlineapp.Presentation.Adapter.MyDeadlineRecyclerViewAdapter
import com.alexandr.deadlineapp.Presentation.Item.DeadlinesViewHolder
import com.alexandr.deadlineapp.R
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.deadline_fragment.*


class DeadlineFragment : Fragment(), View.OnClickListener {

    private lateinit var mainActivity : MainActivity
    private lateinit var viewModel : DeadlineViewModel
    private lateinit var bot: AddBottomSheetDialogFragment
    private lateinit var fm: FragmentManager

    enum class DataType {
        FULL, NOTCOMP, FIND
    }

    private lateinit var type: DataType
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
        bot = AddBottomSheetDialogFragment.newInstance(mainActivity)
        fm = mainActivity.supportFragmentManager
        setRecycler()
        defaultData()
        editDeadline()
        deleteDeadline()
        setToolbar()
        fab.setOnClickListener(this)
    }

    private fun editDeadline() {
        viewModel.edit.observe(viewLifecycleOwner, Observer {
            bot.setEdit(it)
            bot.show(fm,
                AddBottomSheetDialogFragment.TAG
            )
        })
    }

    private fun deleteDeadline() {
        viewModel.delete.observe(viewLifecycleOwner, Observer {
            viewModel.deleteOne(it)
            val snackbar = Snackbar.make(view as View,
                R.string.deleteDeadline, Snackbar.LENGTH_SHORT)
            var isRemoved = true
            snackbar
                .setAction(R.string.cancel) {
                    isRemoved = false
                }
                .addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        super.onDismissed(transientBottomBar, event)
                        if (!isRemoved) {
                            viewModel.saveInformation(it)
                        }
                    }
                })
                .show()
        })
    }

    private fun defaultData() {
        type = DataType.FULL
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
                if (dy > 0) {
                    fab.hide()
                }
                else if (dy < 0) {
                    fab.show()
                }
            }
        })
    }

    private fun noDeadlines(t: Boolean) {
        textViewEmpty.visibility = if (t) View.GONE else View.VISIBLE
    }

    private var simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            return ItemTouchHelper.Callback.makeMovementFlags(
                0,
                ItemTouchHelper.LEFT
            )
        }
        override fun onMove(recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
            if (swipeDir == ItemTouchHelper.LEFT) {
                if (viewHolder is DeadlinesViewHolder) {
                    viewModel.delete(viewHolder.getDeadline())
                }
            }
        }
    }

    override fun onClick(v: View?) {
        bot.show(fm,
            AddBottomSheetDialogFragment.TAG
        )
    }

    private fun unObserve() {
        when (type) {
            DataType.FULL ->  viewModel.clearObserveData(viewLifecycleOwner)
            DataType.FIND -> viewModel.clearObserveFind(viewLifecycleOwner)
            DataType.NOTCOMP -> viewModel.clearObserveDataCur(viewLifecycleOwner)
        }
    }

    private fun setToolbar(){
        mainActivity.setSupportActionBar(toolbar)
        mainActivity.toolbar.title = resources.getString(R.string.title)
        /*mainActivity.toolbar.menu.add("White").setOnMenuItemClickListener {
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
        }*/
    }


}
