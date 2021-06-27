package com.ebookfrenzy.hyrule.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "entries")
data class Data(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val attack: Int?,
    val category: String,
    val common_locations: List<String>?,
    val defense: Int?,
    val description: String,
    val image: String,
    val name: String,
    val cooking_effect: String?,
    val hearts_recovered: Float?,
    val drops: List<String>?
) : Parcelable