package com.noplanb.noplanb.data.viewmodel

import android.text.TextUtils
import com.noplanb.noplanb.data.models.Project

class SharedViewModel {
    fun validProjectDataFromInput(title: String, description: String) : Boolean {
//        return !(TextUtils.isEmpty(title) || TextUtils.isEmpty(description))
        return !TextUtils.isEmpty(title)
    }

    fun validTaskDataFromInput(project: Project, title: String, description: String) : Boolean {
        return !TextUtils.isEmpty(title)
    }
}