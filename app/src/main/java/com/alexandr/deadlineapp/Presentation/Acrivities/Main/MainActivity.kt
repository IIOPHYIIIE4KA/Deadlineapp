package com.alexandr.deadlineapp.Presentation.Acrivities.Main

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.alexandr.deadlineapp.Presentation.Item.TimeNotification
import com.alexandr.deadlineapp.R
import com.alexandr.deadlineapp.Utils.Utils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var bottomSheetBehavior : BottomSheetBehavior<LinearLayout>
    private var touchtoback : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setToolbar(resources.getString(R.string.title))
        fab.setOnClickListener(this)
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_layout)
        setBottom()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        when (newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                this.recreate()
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                this.recreate()
            }
        }
    }

    fun changeColorsDark(){
        bottom_sheet_layout.background = ContextCompat.getDrawable(this, R.color.bottomColor)
        mainAct.background = ContextCompat.getDrawable(this, android.R.color.background_dark)
        toolbar.background = ContextCompat.getDrawable(this, R.color.colorPrimary)
        toolbar.setTitleTextColor(resources.getColor(R.color.textColorPrimary, theme))
        this.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        this.window.statusBarColor = resources.getColor(R.color.colorPrimaryDark, theme)
        this.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        editPredmet.setTextColor(resources.getColor(R.color.textColorPrimary, theme))
        editPredmet.setHintTextColor(resources.getColor(R.color.colorHint, theme))
        chipLow.setTextColor(resources.getColor(R.color.textColorPrimary, theme))
        chipMedium.setTextColor(resources.getColor(R.color.textColorPrimary, theme))
        chipHigh.setTextColor(resources.getColor(R.color.textColorPrimary, theme))
    }

    fun changeColorsLight(){
        bottom_sheet_layout.background = ContextCompat.getDrawable(this, R.color.bottomColor)
        mainAct.background = ContextCompat.getDrawable(this, android.R.color.background_light)
        toolbar.background = ContextCompat.getDrawable(this, R.color.colorPrimary)
        toolbar.setTitleTextColor(resources.getColor(R.color.textColorPrimary, theme))
        this.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        this.window.statusBarColor = resources.getColor(R.color.colorPrimaryDark, theme)
        this.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        editPredmet.setTextColor(resources.getColor(R.color.textColorPrimary, theme))
        editPredmet.setHintTextColor(resources.getColor(R.color.colorHint, theme))
    }
    private fun setToolbar(title: String){
        setSupportActionBar(toolbar)
        supportActionBar?.title = title
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menuWhite -> {
                    AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_NO);
                }
                R.id.menuBlack -> {
                    AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_YES);
                }
            }
            true
        }
    }

    fun bottomShow(){
        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED)
        {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    fun bottomHide(){
        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN)
        {
            val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(bottom_sheet_layout.windowToken, 0)
            Handler().postDelayed(Runnable { bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN }, 50)
        }
    }

    private var bottomSheetCallback: BottomSheetBehavior.BottomSheetCallback = object :
        BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            when (newState) {
                BottomSheetBehavior.STATE_HIDDEN -> {
                    fab.animate().scaleX(1.toFloat()).scaleY(1.toFloat()).setDuration(0).start()
                }
                BottomSheetBehavior.STATE_EXPANDED -> {

                }
                BottomSheetBehavior.STATE_COLLAPSED -> {

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
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)
        imgCal.setOnClickListener {
            DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    val c = Calendar.getInstance()
                    c.set(year,monthOfYear,dayOfMonth)
                    var date: String = "${Utils.WeekDay(c.get(Calendar.DAY_OF_WEEK))}, ${year}."
                    date += if (monthOfYear<10) "0${monthOfYear}." else "${monthOfYear}."
                    date += if (dayOfMonth<10) "0${dayOfMonth}" else "$dayOfMonth"
                    editDate.setText(date)
                }, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show()
        }

        imgClk.setOnClickListener {
            TimePickerDialog(this,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    var time: String = if (hourOfDay<10) "0${hourOfDay}:" else "${hourOfDay}:"
                    time += if (minute<10) "0${minute}" else "$minute"
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
            BottomSheetBehavior.STATE_HIDDEN
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
