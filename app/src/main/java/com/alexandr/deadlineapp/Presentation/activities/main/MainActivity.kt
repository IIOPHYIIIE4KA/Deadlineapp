package com.alexandr.deadlineapp.Presentation.activities.main

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alexandr.deadlineapp.R
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MainActivity : AppCompatActivity() {

    private lateinit var bottomSheetBehavior : BottomSheetBehavior<LinearLayout>
    private var touchtoback : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

    override fun onBackPressed() {
        touchtoback++
        if (touchtoback == 1) {
                Toast.makeText(this, resources.getString(R.string.pressBack), Toast.LENGTH_SHORT).show()
                Handler().postDelayed({ touchtoback-- }, 2000)
        } else { super.onBackPressed(); }
    }

}
