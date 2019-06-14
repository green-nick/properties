package com.github.greennick.properties.debounce

import com.github.greennick.properties.generic.MutableProperty

interface DebounceProperty<T> : MutableProperty<T> {
    fun forceSet(value: T)
}

typealias Executor = (delay: Long, action: () -> Unit) -> Cancellable

typealias Cancellable = () -> Unit

fun <T> debouncePropertyOf(value: T, delay: Long, executor: Executor): DebounceProperty<T> =
    DebouncePropertyImpl(delay, executor, value)