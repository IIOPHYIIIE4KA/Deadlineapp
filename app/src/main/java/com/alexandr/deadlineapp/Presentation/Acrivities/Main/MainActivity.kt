package com.alexandr.deadlineapp.Presentation.Acrivities.Main

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alexandr.deadlineapp.Presentation.Item.TimeNotification
import com.alexandr.deadlineapp.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet.*

class MainActivity : AppCompatActivity() {

    private var touchtoback: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Дедлайны"
        var bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_layout)
        fab.setOnClickListener {
            bottomShow()
        }
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(bottom_sheet_layout.windowToken, 0)
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {

                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        clear()
                        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(bottom_sheet_layout.windowToken, 0)
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
        })
    }

    fun bottomShow(){
        var bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_layout)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    fun bottomHide(){
        var bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_layout)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    fun clear(){
        editDate.text.clear()
        editTime.text.clear()
        editDescription.text.clear()
        editPredmet.text.clear()
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
                Toast.makeText(this, "Нажмите НАЗАД еще раз для выхода", Toast.LENGTH_SHORT).show()
                Handler().postDelayed(Runnable { touchtoback-- }, 2000)
        } else { super.onBackPressed(); }
    }

}
