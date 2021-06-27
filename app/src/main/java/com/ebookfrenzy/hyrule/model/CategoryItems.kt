package com.ebookfrenzy.hyrule.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryItems(
    val items: List<CategoryItem>
): Parcelable
