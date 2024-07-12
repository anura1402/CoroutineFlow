package ru.anura.coroutineflow.lessons.lesson6

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

val coroutineScope = CoroutineScope(Dispatchers.IO)

suspend fun main() {
    val flow = MutableSharedFlow<Int>()

    coroutineScope.launch {
        repeat(5){
            println("Emitting $it")
            flow.emit(it)
            delay(1000)
        }
    }
    val job1 = coroutineScope.launch {
        //1.flow будет эмиттить значения, даже если нет подписчиков
        //flow.collect {
        //3.если подписчикам больше не нужны данные, то flow продолжает эмиттить значения
        flow.first().let {
            println(it)
        }
    }
    delay(5000)
    //2. подписчики получают одни и те же значения
    //вывод начнется с того числа, которое эмиттилось после 5 секунд
    val job2 = coroutineScope.launch {
        flow.collect {
            println(it)
        }
    }
    //0.Hot Flow никогда не завершится и при join программа будет работать всегда
    job1.join()
    job2.join()
}
