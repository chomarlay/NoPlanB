package com.noplanb.noplanb.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity (foreignKeys = arrayOf(ForeignKey(entity = Project::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("projectId"),
                        onDelete = ForeignKey.CASCADE)),
    indices = arrayOf(
        Index(
            value = ["projectId"]
        )
    )
    )
@Parcelize

data class Task(
    @PrimaryKey( autoGenerate = true)
    var id: Int,
    var projectId: Int,
    var title: String,
    var description: String,
    var dueDate: Date?,
    var completedDate: Date?
): Parcelable

