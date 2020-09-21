package com.noplanb.noplanb.data.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TaskWithProject (
    @Embedded val task: Task,
    @Relation(
        parentColumn = "projectId",
        entityColumn = "id"
    )
    val project: Project

):Parcelable