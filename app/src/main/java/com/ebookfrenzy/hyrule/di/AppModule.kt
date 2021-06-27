package com.ebookfrenzy.hyrule.di

import android.content.Context
import com.ebookfrenzy.hyrule.api.HyruleService
import com.ebookfrenzy.hyrule.db.EntryDao
import com.ebookfrenzy.hyrule.db.HyruleDatabase
import com.ebookfrenzy.hyrule.repository.HyruleRepository
import com.ebookfrenzy.hyrule.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    @Singleton
    @Provides
    fun provideRepository(hyruleService: HyruleService, entryDao: EntryDao): HyruleRepository {
        return HyruleRepository(hyruleService, entryDao)
    }

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
    }

    @Singleton
    @Provides
    fun provideHyruleService(client: OkHttpClient): HyruleService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(HyruleService::class.java)
    }

    @Singleton
    @Provides
    fun provideEntryDaoImpl(@ApplicationContext appContext: Context): EntryDao {
        return HyruleDatabase.getInstance(appContext).entryDao()
    }
}
