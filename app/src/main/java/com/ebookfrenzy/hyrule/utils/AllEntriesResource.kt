package com.ebookfrenzy.hyrule.utils

sealed class AllEntriesResource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T): AllEntriesResource<T>(data)
    class Failure<T>(message: String): AllEntriesResource<T>(null, message)
    class Loading<T>: AllEntriesResource<T>()
}