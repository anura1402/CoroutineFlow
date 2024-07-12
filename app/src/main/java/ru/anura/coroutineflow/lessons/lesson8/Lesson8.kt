package ru.anura.coroutineflow.lessons.lesson8

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch


suspend fun main(){
    val scope = CoroutineScope(Dispatchers.Default)
    val job = scope.launch {
        val flow: Flow<Int> = flow {
            repeat(10){
                println("Emitting $it")
                emit(it)
                println("Emit done $it")
                delay(200)
            }
        }.buffer() //размер 64 элемента, при переполнении корутина приостанавливается, пока буффер не освободится
        flow.collect{
            println("Collected $it")
            delay(1000)
        }
    }
    job.join()

}