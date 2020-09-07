package com.noplanb.noplanb.data.models

import androidx.room.Embedded
import androidx.room.Relation

data class ProjectWithTasks (
    @Embedded val project: Project,
    @Relation(
        parentColumn = "id",
        entityColumn = "projectId"
    )
    val tasks: List<Task>

)