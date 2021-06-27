package com.ebookfrenzy.hyrule.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Material(
    val category: String,
    val common_locations: List<String>,
    val cooking_effect: String,
    val description: String,
    val hearts_recovered: Float,
    val id: Int,
    val image: String,
    val name: String
): Parcelable