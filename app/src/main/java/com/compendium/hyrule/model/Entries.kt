package com.compendium.hyrule.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Entries(
    val `data`: List<Data>
): Parcelable