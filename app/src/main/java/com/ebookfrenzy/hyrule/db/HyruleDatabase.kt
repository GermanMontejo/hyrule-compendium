package com.ebookfrenzy.hyrule.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ebookfrenzy.hyrule.model.Data

@TypeConverters(Converters::class)
@Database(entities = [Data::class], version = 1)
abstract class HyruleDatabase : RoomDatabase() {
    abstract fun entryDao(): EntryDao

    companion object {
        @Volatile
        private lateinit var instance: HyruleDatabase
        fun getInstance(context: Context): HyruleDatabase {
            if (!this::instance.isInitialized) {
                synchronized(HyruleDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        HyruleDatabase::class.java,
                        "hyrule.db"
                    ).build()
                }
            }
            println("returning instance!")
            return instance
        }
    }
}