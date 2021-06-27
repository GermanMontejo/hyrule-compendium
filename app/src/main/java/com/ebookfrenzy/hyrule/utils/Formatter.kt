package com.ebookfrenzy.hyrule.utils

object Formatter {
    fun removeBrackets(items: List<String>?): String {
        return capitalizeFirstLetters(items.toString().replace("[", "").replace("]", ""))
    }

    fun capitalizeFirstLetters(name: String): String {
        return name.split(" ").joinToString(" ") {
            it.replaceFirstChar { text ->
                if (text.isLowerCase()) text.titlecase() else text.toString()
            }
        }
    }
}