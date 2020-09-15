package com.noplanb.noplanb.data.models

import android.os.Parcelable
import androidx.room.Entity

import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity
@Parcelize
data class Task(
    @PrimaryKey( autoGenerate = true)
    var id: Int,
    var projectId: Int,
    var title: String,
    var description: String,
    var dueDate: Date?
): Parcelable