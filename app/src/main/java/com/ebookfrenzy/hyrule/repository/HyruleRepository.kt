package com.ebookfrenzy.hyrule.repository

import com.ebookfrenzy.hyrule.api.HyruleService
import com.ebookfrenzy.hyrule.db.EntryDao
import com.ebookfrenzy.hyrule.model.Creatures
import com.ebookfrenzy.hyrule.model.Data
import com.ebookfrenzy.hyrule.model.Entries
import retrofit2.Response
import javax.inject.Inject

class HyruleRepository @Inject constructor(
    val hyruleService: HyruleService,
    val entryDao: EntryDao
) {
    // API
    suspend fun getEntry(entry: String) = hyruleService.getEntry(entry)
    suspend fun getEntriesByCategory(category: String): Response<Entries> =
        hyruleService.getEntriesByCategory(category)

    suspend fun getCreatures(): Response<Creatures> = hyruleService.getCreatures()

    // DB
    suspend fun upsertEntry(data: Data): Int {
        var rowId = entryDao.insert(data)
        if (rowId == -1L) {
            // this means there was a conflict while inserting the entry, so we're gonna call update
            return entryDao.update(data)
        }
        return rowId.toInt()
    }
    suspend fun deleteEntry(data: Data) = entryDao.deleteEntry(data)
    fun getSavedEntries() = entryDao.getSavedEntries()
    suspend fun getSavedEntryByName(name: String) = entryDao.getEntryByName(name)
    suspend fun isEntrySaved(id: Int): Boolean {
        if (entryDao.getEntryById(id) != null)
            return true
        return false
    }
}