package com.noplanb.noplanb.data.models

import androidx.room.Embedded
import androidx.room.Relation

data class TaskWithProject (
    @Embedded val task: Task,
    @Relation(
        parentColumn = "projectId",
        entityColumn = "id"
    )
    val project: Project

)