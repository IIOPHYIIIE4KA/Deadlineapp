package com.alexandr.deadlineapp.presentation.activities.main

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexandr.deadlineapp.domain.DeadlineViewModel
import com.alexandr.deadlineapp.presentation.adapter.MyDeadlineRecyclerViewAdapter
import com.alexandr.deadlineapp.presentation.item.DeadlinesViewHolder
import com.alexandr.deadlineapp.R
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.deadline_fragment.*
import kotlin.math.roundToInt


class DeadlineFragment : Fragment(), View.OnClickListener {

    private lateinit var mainActivity: MainActivity
    private lateinit var viewModel: DeadlineViewModel
    private lateinit var bot: AddBottomSheetDialogFragment
    private lateinit var fm: FragmentManager
    private lateinit var vibrator: Vibrator
    private var isVibrated = false

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
        vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
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

        override fun onMove(recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
        ): Boolean {
            if (viewHolder is DeadlinesViewHolder) {
                viewHolder.closeContextMenu()
            }
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
            if (swipeDir == ItemTouchHelper.LEFT) {
                if (viewHolder is DeadlinesViewHolder) {
                    viewHolder.closeContextMenu()
                    viewModel.delete(viewHolder.getDeadline())
                }
            }
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            val trashBinIcon = resources.getDrawable(
                R.drawable.ic_delete_black_24dp,
                mainActivity.theme
            )
            //c.clipRect(viewHolder.itemView.width.toFloat(), viewHolder.itemView.top.toFloat(),
            //    dX, viewHolder.itemView.bottom.toFloat())
            val textMargin = resources.getDimension(R.dimen.text_margin)
                .roundToInt() * 4
            trashBinIcon.bounds = Rect(
                viewHolder.itemView.width - trashBinIcon.intrinsicWidth + textMargin + dX.toInt(),
                viewHolder.itemView.top +
                        (viewHolder.itemView.height - trashBinIcon.intrinsicHeight) / 2,
                viewHolder.itemView.width + textMargin + dX.toInt(),
                viewHolder.itemView.top +
                        (viewHolder.itemView.height + trashBinIcon.intrinsicHeight) / 2
            )
            if (-dX.toInt() >= viewHolder.itemView.width / 2 && isVibrated) {
                isVibrated = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(
                            10,
                            VibrationEffect.EFFECT_TICK
                        ))
                } else {
                    vibrator.vibrate(10)
                    // api 23??
                }
            }
            if (-dX.toInt() < viewHolder.itemView.width / 2) {
                isVibrated = true
            }
            if (-dX.toInt() != viewHolder.itemView.width) {
                val paint = Paint()
                paint.style = Paint.Style.FILL_AND_STROKE
                paint.color = Color.RED
                c.drawCircle(
                    viewHolder.itemView.width - trashBinIcon.intrinsicWidth/2 + textMargin + dX,
                    (viewHolder.itemView.top + viewHolder.itemView.height / 2).toFloat(),
                    -dX/5,
                    paint
                )
                trashBinIcon.draw(c)
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
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
