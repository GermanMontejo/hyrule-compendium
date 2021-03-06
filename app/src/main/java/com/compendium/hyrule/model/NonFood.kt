package com.compendium.hyrule.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NonFood(
    val category: String,
    val common_locations: List<String>,
    val description: String,
    val drops: List<String>,
    val id: Int,
    val image: String,
    val name: String
): Parcelable