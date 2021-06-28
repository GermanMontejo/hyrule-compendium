package com.compendium.hyrule.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Creature(
    val food: List<Food>,
    val non_food: List<NonFood>
): Parcelable