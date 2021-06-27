package com.ebookfrenzy.hyrule.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ebookfrenzy.hyrule.model.Data

@Dao
interface EntryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(data: Data): Long

    @Update
    suspend fun update(data: Data): Int

    @Query("SELECT * FROM entries")
    fun getSavedEntries(): LiveData<List<Data>>

    @Delete
    suspend fun deleteEntry(data: Data)

    @Query("SELECT * FROM entries WHERE name = :name")
    suspend fun getEntryByName(name: String): Data

    @Query("SELECT * FROM entries WHERE id = :id")
    suspend fun getEntryById(id: Int): Data?
}