package com.compendium.hyrule.api

import com.compendium.hyrule.model.Creatures
import com.compendium.hyrule.model.Entries
import com.compendium.hyrule.model.Entry
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface HyruleService {
    @GET("/api/v2/entry/{entry}")
    suspend fun getEntry(@Path("entry") entry: String): Response<Entry>

    @GET("/api/v2/category/{category}")
    suspend fun getEntriesByCategory(@Path("category") category: String): Response<Entries>

    @GET("/api/v2/category/creatures")
    suspend fun getCreatures(): Response<Creatures>
}