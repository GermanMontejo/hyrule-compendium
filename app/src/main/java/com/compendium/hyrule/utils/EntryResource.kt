package com.compendium.hyrule.utils

sealed class EntryResource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : EntryResource<T>(data)
    class Failure<T>(message: String) : EntryResource<T>(null, message)
    class Loading<T> : EntryResource<T>()
}