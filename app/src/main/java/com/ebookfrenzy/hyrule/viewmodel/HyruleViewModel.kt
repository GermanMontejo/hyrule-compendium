package com.ebookfrenzy.hyrule.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ebookfrenzy.hyrule.HyruleApplication
import com.ebookfrenzy.hyrule.model.*
import com.ebookfrenzy.hyrule.repository.HyruleRepository
import com.ebookfrenzy.hyrule.utils.EntryResource
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HyruleViewModel @Inject constructor(
    private val repository: HyruleRepository,
    val app: Application
) : AndroidViewModel(app) {

    private val _entryLiveData = MutableLiveData<ConsumableEvent<EntryResource<Entry>>>()
    val entryLiveData: LiveData<ConsumableEvent<EntryResource<Entry>>>
        get() = _entryLiveData

    private val _entriesLiveData = MutableLiveData<ConsumableEvent<EntryResource<Entries>>>()
    val entriesLiveData: LiveData<ConsumableEvent<EntryResource<Entries>>>
        get() = _entriesLiveData

    private val _creaturesLiveData = MutableLiveData<ConsumableEvent<EntryResource<Creatures>>>()
    val creaturesLiveData: LiveData<ConsumableEvent<EntryResource<Creatures>>>
        get() = _creaturesLiveData

    private val _savedEntry = MutableLiveData<ConsumableEvent<Data>>()
    val savedEntry: LiveData<ConsumableEvent<Data>>
        get() = _savedEntry

    // this is the result of the upsertEntry() call
    private val _result = MutableLiveData<Int>()
    val result: LiveData<Int>
        get() = _result



    fun getEntry(entry: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _entryLiveData.postValue(ConsumableEvent(EntryResource.Loading()))
            try {
                if (hasInternetConnection()) {
                    val response = repository.getEntry(entry)
                    if (response.isSuccessful) {
                        response.body()?.let { entry ->
                            _entryLiveData.postValue(ConsumableEvent(EntryResource.Success(entry)))
                        }
                    } else {
                        _entryLiveData.postValue(ConsumableEvent(EntryResource.Failure(response.message())))
                    }
                } else {
                    _entryLiveData.postValue(ConsumableEvent(EntryResource.Failure("No internet connection.")))
                }
            } catch (ioe: IOException) {
                _entryLiveData.postValue(ConsumableEvent(EntryResource.Failure("${ioe.message}")))
            }
        }
    }

    fun getEntriesByCategory(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _entriesLiveData.postValue(ConsumableEvent(EntryResource.Loading()))
            try {
                if (hasInternetConnection()) {
                    if (category == "creatures") {
                        val creatures = repository.getCreatures()
                        if (creatures.isSuccessful) {
                            creatures.body()?.let { foodAndNonFood ->
                                _creaturesLiveData.postValue(
                                    ConsumableEvent(
                                        EntryResource.Success(
                                            foodAndNonFood
                                        )
                                    )
                                )
                            }
                        } else {
                            _creaturesLiveData.postValue(
                                ConsumableEvent(
                                    EntryResource.Failure(
                                        creatures.message()
                                    )
                                )
                            )
                        }
                    } else {
                        val entries = repository.getEntriesByCategory(category)
                        if (entries.isSuccessful) {
                            entries.body()?.let {
                                _entriesLiveData.postValue(ConsumableEvent(EntryResource.Success(it)))
                            }
                        } else {
                            _entriesLiveData.postValue(ConsumableEvent(EntryResource.Failure(entries.message())))
                        }
                    }
                } else {
                    _entriesLiveData.postValue(ConsumableEvent(EntryResource.Failure("No internet connection.")))
                }
            } catch (ioe: IOException) {
                _entriesLiveData.postValue(ConsumableEvent(EntryResource.Failure("An error occurred: ${ioe.message}")))
            }

        }
    }

    fun insertEntry(data: Data) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.upsertEntry(data)
            _result.postValue(result)
        }
    }

    fun deleteEntry(data: Data) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteEntry(data)
        }
    }

    fun getSavedEntry(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val entry = repository.getSavedEntryByName(name)
            _savedEntry.postValue(ConsumableEvent(entry))
        }
    }

    fun isEntrySaved(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.isEntrySaved(id)
        }
    }

    fun getSavedEntries() = repository.getSavedEntries()

    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<HyruleApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
}