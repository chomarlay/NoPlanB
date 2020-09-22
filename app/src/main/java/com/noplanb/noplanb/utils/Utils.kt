package com.noplanb.noplanb.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import java.util.*

fun hideKeyboard(activity: Activity) {
    val inputMethodManager =
        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    val currentFocusedView = activity.currentFocus
    currentFocusedView?.let {
        inputMethodManager.hideSoftInputFromWindow(
            currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}

fun isDueDateOverdue(dueDate: Date?): Boolean {
    val calendar = Calendar.getInstance()
    calendar[Calendar.HOUR_OF_DAY] = 0
    calendar[Calendar.MINUTE] = 0
    calendar[Calendar.SECOND] = 0
    calendar[Calendar.MILLISECOND] = 0
    return (dueDate != null && dueDate!! < calendar.time)

}

fun dueBeforeDate(noDays: Int): Date{
    val calendar = Calendar.getInstance()
    calendar[Calendar.DAY_OF_MONTH] = calendar[Calendar.DAY_OF_MONTH]+noDays
    calendar[Calendar.HOUR_OF_DAY] = 0
    calendar[Calendar.MINUTE] = 0
    calendar[Calendar.SECOND] = 0
    calendar[Calendar.MILLISECOND] = 0
    return calendar.time
}

fun dueDateToSave(year: Int, month: Int, day:Int): Date? {
    if (year == 0) {
        return null
    }
    val calendar = Calendar.getInstance()
    calendar.set(year,month,day,23,59,59)
    return calendar.time
}