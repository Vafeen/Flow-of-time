package ru.vafeen.domain.utils

import kotlinx.coroutines.runBlocking

fun currentTimeSeconds() = System.currentTimeMillis()
fun main() = runBlocking {
    val x = currentTimeSeconds()
//    delay(200)
    val y = currentTimeSeconds()
    println(x)
    println(y)
}