package com.noplanb.noplanb.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Project(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title: String,
    var description: String
): Parcelable {

    override fun toString(): String {
        return title
    }
}