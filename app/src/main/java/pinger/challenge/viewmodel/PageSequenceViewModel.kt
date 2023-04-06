package pinger.challenge.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import pinger.challenge.data.PageSequenceRepository
import pinger.challenge.intent.PageSequenceIntent

class PageSequenceViewModel(private val pageSequenceRepository: PageSequenceRepository) :
    ViewModel() {

    private val _pageSequenceData = MutableLiveData<List<Pair<String, Int>>>()
    val pageSequenceData: LiveData<List<Pair<String, Int>>> = _pageSequenceData

    private val _loadingData = MutableLiveData<Boolean>()
    val loadingData: LiveData<Boolean> = _loadingData

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun processIntent(intent: PageSequenceIntent) {
        when (intent) {
            PageSequenceIntent.FetchLogsIntent -> fetchLogs()
        }
    }

    private fun fetchLogs() = viewModelScope.launch {
        _loadingData.value = true
        try {
            pageSequenceRepository.fetchLogs()
                .catch { e ->
                    _error.value = e.message.toString()
                    _loadingData.value = false
                }
                .collect {
                    _pageSequenceData.value = it
                    _loadingData.value = false
                }
        } catch (e: Exception) {
            _error.value = e.message.toString()
            _loadingData.value = false

        }
    }
}