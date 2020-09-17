package com.noplanb.noplanb.data.viewmodel

import android.text.TextUtils
import com.noplanb.noplanb.data.models.Project
import java.util.*

class SharedViewModel {
    fun validProjectDataFromInput(title: String, description: String) : Boolean {
//        return !(TextUtils.isEmpty(title) || TextUtils.isEmpty(description))
        return !TextUtils.isEmpty(title)
    }

    fun validTaskDataFromInput(project: Project, title: String, description: String) : Boolean {
        return !TextUtils.isEmpty(title)
    }

    fun formatDate (calendar: Calendar) : String {
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val year = calendar.get(Calendar.YEAR)
        return (formatDate(year,month, day))

    }

    fun formatDate (year: Int, month: Int, day: Int) : String {
        val month = String.format("%02d", month);
        val day = String.format("%02d", day);
        return "${day}-${month}-${year}"
    }
}