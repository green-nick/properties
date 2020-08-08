package com.github.greennick.properties

import com.github.greennick.properties.generic.Property
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

interface PropertyEmitter<in T> {
    fun emit(value: T)
}

fun <T> CoroutineScope.propertyOf(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend PropertyEmitter<T>.() -> Unit
): Property<T?> {
    val prop = emptyProperty<T?>()
    launch(context) {
        val emitter = object : PropertyEmitter<T> {
            override fun emit(value: T) = prop.set(value)
        }
        emitter.block()
    }
    return prop
}

fun <T> CoroutineScope.propertyOf(
    init: T,
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend PropertyEmitter<T>.() -> Unit
): Property<T> {
    val prop = propertyOf(init)
    launch(context) {
        val emitter = object : PropertyEmitter<T> {
            override fun emit(value: T) = prop.set(value)
        }
        emitter.block()
    }
    return prop
}

fun main() = runBlocking<Unit> {
    val flow = flow {
        emit(load())
        delay(1000)
        emit("world")
    }
    val prop = propertyOf("one") {
        flow.collect { emit(it) }
    }
    prop.subscribe(::println)
}

suspend fun load(): String {
    delay(1000)
    return "Hello"
}
