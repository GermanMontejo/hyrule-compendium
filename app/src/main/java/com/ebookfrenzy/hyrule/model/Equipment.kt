package com.ebookfrenzy.hyrule.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Equipment(
    val attack: Int,
    val category: String,
    val common_locations: List<String>,
    val defense: Int,
    val description: String,
    val id: Int,
    val image: String,
    val name: String
): Parcelable