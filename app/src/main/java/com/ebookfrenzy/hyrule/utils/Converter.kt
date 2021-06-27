package com.ebookfrenzy.hyrule.utils

import com.ebookfrenzy.hyrule.model.CategoryItem
import com.ebookfrenzy.hyrule.model.Creature
import com.ebookfrenzy.hyrule.model.Data
import com.ebookfrenzy.hyrule.model.Entries

object Converter {

    fun convertEntriesToCategoryItems(entries: Entries): MutableList<CategoryItem> {
        val categoryItems = mutableListOf<CategoryItem>()
        for (entry in entries.data) {

            categoryItems.add(
                createCategoryItem(entry)
            )
            println("categoryItems: ${categoryItems[0]}")
        }
        return categoryItems
    }

    fun createCategoryItem(entry: Data): CategoryItem {
        var info1 = ""
        var info2 = ""
        when (entry.category) {
            "monsters" -> {
                var drops = Formatter.removeBrackets(entry.drops)
                info1 = "Drops: ${if (drops.isBlank()) "N/A" else drops}"
            }
            "equipment" -> {
                info1 = "Attack: ${entry.attack}, Defense: ${entry.defense}"
            }
            "materials" -> {
                info1 = "Cooking Effect: ${entry.cooking_effect}"
            }
            "treasure" -> {
                info1 = "Drops: ${Formatter.removeBrackets(entry.drops)}"
            }
        }
        info2 = "Locations: ${Formatter.removeBrackets(entry.common_locations)}"
        return CategoryItem(
            entry.id,
            entry.name,
            info1,
            info2,
            entry.category,
            entry.image
        )
    }

    fun convertCreaturesToCategoryItems(creatures: Creature): MutableList<CategoryItem> {
        val categoryItems = mutableListOf<CategoryItem>()
        for (item in creatures.food) {
            categoryItems.add(
                CategoryItem(
                    item.id,
                    item.name,
                    "Cooking Effect: ${item.cooking_effect}",
                    "Locations: ${Formatter.removeBrackets(item.common_locations)}",
                    item.category,
                    item.image
                )
            )
        }
        for (item in creatures.non_food) {
            categoryItems.add(
                CategoryItem(
                    item.id,
                    item.name,
                    "Drops: ${Formatter.removeBrackets(item.drops)}",
                    "Locations: ${Formatter.removeBrackets(item.common_locations)}",
                    item.category,
                    item.image
                )
            )
        }
        return categoryItems
    }

    fun convertEntryToCategoryItems(entry: Data): MutableList<CategoryItem> {
        return mutableListOf(createCategoryItem(entry))
    }
}