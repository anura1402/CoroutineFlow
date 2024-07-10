package ru.anura.coroutineflow.lessons.crypto_app_lesson_4

sealed class State {
    object Initial : State()
    object Loading : State()
    data class Content(val currencyList: List<Currency>) : State()
}
