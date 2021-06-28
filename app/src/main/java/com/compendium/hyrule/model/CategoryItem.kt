package com.compendium.hyrule.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryItem(
    val id: Int,
    val name: String,
    val info1: String,
    val info2: String,
    val category: String,
    val imageUrl: String
) : Parcelable