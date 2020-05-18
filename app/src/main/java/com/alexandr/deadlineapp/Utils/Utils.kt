package com.alexandr.deadlineapp.Utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar


class Utils {
    companion object{
        fun WeekDay(day:Int):String{
            return when(day) {
                1 -> "Вс"
                2 -> "Пн"
                3 -> "Вт"
                4 -> "Ср"
                5 -> "Чт"
                6 -> "Пт"
                7 -> "Сб"
                else -> "Пн"
            }
        }

        fun makeToast(msg: String, context: Context){
            Toast.makeText(context,msg,Toast.LENGTH_SHORT)
        }

        fun makeSnack(msg: String, v: View){
            Snackbar.make(v,msg,Snackbar.LENGTH_SHORT)
        }

        fun hideKeyboard(activity: Activity) {
            val imm: InputMethodManager =
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            var view = activity.currentFocus
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}