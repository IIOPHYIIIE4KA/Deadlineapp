package com.alexandr.deadlineapp.Presentation.Acrivities.Main

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import com.alexandr.deadlineapp.Presentation.Item.TimeNotification
import com.alexandr.deadlineapp.R
import com.alexandr.deadlineapp.Utils.Utils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private var touchtoback: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setToolbar(resources.getString(R.string.title))
        fab.setOnClickListener(this)
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_layout)
        setBottom()
    }

    private fun setToolbar(title: String){
        setSupportActionBar(toolbar)
        supportActionBar?.title = title
    }

    fun bottomShow(){
        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED)
        {bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED}
    }

    fun bottomHide(){
        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_COLLAPSED)
        {bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED}
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
                    clear()
                }
                BottomSheetBehavior.STATE_DRAGGING -> {

                }
                BottomSheetBehavior.STATE_SETTLING -> {
                }
            }
        }
        override fun onSlide(
            bottomSheet: View,
            slideOffset: Float
        ) {
            fab.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
        }
    }

    private fun setBottom(){
        bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)
        imgCal.setOnClickListener {
            DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    val c = Calendar.getInstance()
                    c.set(year,monthOfYear,dayOfMonth)
                    var date: String = "${Utils.WeekDay(c.get(Calendar.DAY_OF_WEEK))}, ${year}."
                    date += if (monthOfYear<10){
                        "0${monthOfYear}."
                    } else { "${monthOfYear}." }
                    date += if (dayOfMonth<10){
                        "0${dayOfMonth}"
                    } else { "$dayOfMonth"   }
                    editDate.setText(date)
                }, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show()
        }

        imgClk.setOnClickListener {
            TimePickerDialog(this,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    var time: String = if (hourOfDay<10){
                        "0${hourOfDay}:"
                    } else { "${hourOfDay}:" }
                    time += if (minute<10){
                        "0${minute}"
                    } else { "$minute" }
                    editTime.setText(time)
                }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),true).show()
        }

        imgPinned.setOnClickListener { it as ImageView
            if (it.contentDescription == "pinned"){
                it.setImageResource(R.drawable.ic_unpin)
                it.contentDescription = "unpinned"
            } else {
                it.setImageResource(R.drawable.ic_pin)
                it.contentDescription = "pinned"
            }
        }
    }

    fun clear(){
        editDate.text.clear()
        editTime.text.clear()
        editDescription.text.clear()
        editPredmet.text.clear()
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(bottom_sheet_layout.windowToken, 0)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                this.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun restartNotify(){
        val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this,TimeNotification::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this,0,
            intent, PendingIntent.FLAG_CANCEL_CURRENT)
        //am.cancel(pendingIntent)
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent)
    }

    override fun onBackPressed() {
        touchtoback++
        val bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_layout)
        if (bottomSheetBehavior.state !=
            BottomSheetBehavior.STATE_COLLAPSED
        ) {
            bottomHide()
            touchtoback = 0;
            return;
        }
        if (touchtoback == 1) {
                Toast.makeText(this, resources.getString(R.string.pressBack), Toast.LENGTH_SHORT).show()
                Handler().postDelayed(Runnable { touchtoback-- }, 2000)
        } else { super.onBackPressed(); }
    }

    override fun onClick(v: View?) {
        bottomShow()
    }

}
