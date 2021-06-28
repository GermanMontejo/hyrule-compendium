package com.compendium.hyrule.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Creatures(
    val `data`: Creature
): Parcelable
