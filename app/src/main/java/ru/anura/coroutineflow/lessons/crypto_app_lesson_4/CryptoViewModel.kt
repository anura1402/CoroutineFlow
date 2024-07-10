package ru.anura.coroutineflow.lessons.crypto_app_lesson_4

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class CryptoViewModel : ViewModel() {

    private val repository = CryptoRepository

    private val _state = MutableLiveData<State>(State.Initial)
    val state: LiveData<State> = _state

    init {
        loadData()
    }

    private fun loadData() {
//        viewModelScope.launch {
//            repository.getCurrencyList()
//                .onStart {
//                    val currentState = _state.value
//                    if (currentState !is State.Content || currentState.currencyList.isEmpty()) {
//                        _state.value = State.Loading
//                    }
//                }
//                .filter { it.isNotEmpty() }
//                .onEach { _state.value = State.Content(currencyList = it) }
//                .collect()
//        }
            repository.getCurrencyList()
                .onStart {
                    val currentState = _state.value
                    if (currentState !is State.Content || currentState.currencyList.isEmpty()) {
                        _state.value = State.Loading
                    }
                }
                .filter { it.isNotEmpty() }
                .onEach { _state.value = State.Content(currencyList = it) }
                .launchIn(viewModelScope)
    }
}
