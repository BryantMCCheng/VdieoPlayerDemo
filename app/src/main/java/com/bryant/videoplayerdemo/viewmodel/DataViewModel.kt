package com.bryant.videoplayerdemo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bryant.videoplayerdemo.DataRepository
import com.bryant.videoplayerdemo.data.P
import com.bryant.videoplayerdemo.extensions.TAG
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber

class DataViewModel(private val repository: DataRepository) : ViewModel() {

    private var _dataInfo = MutableLiveData<List<P?>>()
    val dataInfo: LiveData<List<P?>> = _dataInfo
    private var _pageAction = MutableLiveData<String>()
    val pageAction: LiveData<String> = _pageAction

    fun updatePageIndex(action: String) {
        _pageAction.value = action
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(TAG, "error: ${throwable.localizedMessage}")
    }

    init {
        getVideoDataList()
    }

    private fun getVideoDataList() {
        viewModelScope.launch(exceptionHandler) {
            val response = repository.getVideoDataList()
            if (response.isSuccessful) {
                Timber.d(TAG, "response = ${response.body()?.p.toString()}")
                response.body()?.p?.let {
                    _dataInfo.value = it
                }
            } else {
                Timber.e(TAG, "response = ${response.message()}")
            }
        }
    }

    class Factory(private val repository: DataRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DataViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DataViewModel(repository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}