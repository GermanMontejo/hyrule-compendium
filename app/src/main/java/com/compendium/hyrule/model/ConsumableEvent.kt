package com.compendium.hyrule.model

class ConsumableEvent<T>(private val data: T) {
    private var isConsumed = false

    fun consume(block: (T) -> Unit) {
        if (!isConsumed) {
            isConsumed = true
            block(data)
        }
    }
}