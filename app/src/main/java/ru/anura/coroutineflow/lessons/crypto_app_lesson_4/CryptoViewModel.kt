package ru.anura.coroutineflow.lessons.crypto_app_lesson_4

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class CryptoViewModel : ViewModel() {

//    private val repository = CryptoRepository
//
//
//           val state: LiveData<State> = repository.getCurrencyList()
//                .filter { it.isNotEmpty() }
//                .map { State.Content(currencyList = it) as State}
//                .onStart { emit(State.Loading) }
//                .asLiveData()

    private val repository = CryptoRepository

    private val _state = MutableLiveData<State>(State.Initial)
    val state: LiveData<State> = _state

    private var job: Job? = null
    private var isResumed= false

    init {
        loadData()
    }

    fun loadData() {
        isResumed = true
        if (job!=null) return
        job = repository.getCurrencyList()
            .onStart {
                Log.d("CryptoViewModel", "Started")
                _state.value = State.Loading
            }
            .filter { it.isNotEmpty() }
            .onEach {
                Log.d("CryptoViewModel", "onEach")
                _state.value = State.Content(currencyList = it)
            }
            .onCompletion {
                Log.d("CryptoViewModel", "Completed")
            }
            .launchIn(viewModelScope)
    }

    fun stopLoading(){
        viewModelScope.launch {
            delay(5000)
            if (!isResumed){
                job?.cancel()
                job = null
            } else{
                isResumed = false
            }

        }


    }

}
