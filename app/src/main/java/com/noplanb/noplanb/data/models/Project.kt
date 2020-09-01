package com.noplanb.noplanb.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Project(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title: String,
    var description: String
)