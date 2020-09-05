package com.noplanb.noplanb.data.viewmodel

import android.text.TextUtils

class SharedViewModel {
    fun validProjectDataFromInput(title: String, description: String) : Boolean {
//        return !(TextUtils.isEmpty(title) || TextUtils.isEmpty(description))
        return !TextUtils.isEmpty(title)
    }

}