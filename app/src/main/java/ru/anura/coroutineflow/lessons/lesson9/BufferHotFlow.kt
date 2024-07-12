package ru.anura.coroutineflow.lessons.lesson8

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

suspend fun main(){
    val scope = CoroutineScope(Dispatchers.Default)

    val flow = MutableSharedFlow<Int>()

    val producer = scope.launch {
        delay(500)
        repeat(10){
            println("Emitting $it")
            flow.emit(it)
            println("Emit done $it")
            delay(200)
        }
    }

    val consumer = scope.launch {
        flow.collectLatest{
            println("Collected $it")
            delay(1000)
        }
    }

    producer.join()
    consumer.join()

}