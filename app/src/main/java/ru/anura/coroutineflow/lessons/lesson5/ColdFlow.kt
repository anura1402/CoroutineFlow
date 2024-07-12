package ru.anura.coroutineflow.lessons.lesson5

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

val coroutineScope = CoroutineScope(Dispatchers.IO)

//0.если main закончит выполнение раньше coroutineScope, то скоупы не завершатся
suspend fun main() {
    val flow = getFlow()
    val job1 = coroutineScope.launch {
        //1.на flow обязательно нужно подписываться тернарным оператором, чтобы он выполнялся
        //flow.collect {
        //3.если подписчикам больше не нужны данные, то flow прекращает работу
        flow.first().let {
            println(it)
        }
    }
    //2. на каждую подписку создается новый поток
    //вывод начнется с 0
    val job2 = coroutineScope.launch {
        flow.collect {
            println(it)
        }
    }
    //0.это поможет main подождать выполнения coroutineScope
    job1.join()
    job2.join()
}

fun getFlow(): Flow<Int> = flow {
    repeat(100) {
        println("Emitting $it")
        emit(it)
        delay(1000)
    }
}