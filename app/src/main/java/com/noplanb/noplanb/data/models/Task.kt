package com.noplanb.noplanb.data.models

import androidx.room.Entity

import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey( autoGenerate = true)
    var id: Int,
    var projectId: Int,
    var title: String,
    var description: String
)