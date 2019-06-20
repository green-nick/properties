package com.github.greennick.properties.debounce

import com.github.greennick.properties.generic.MutableProperty

interface DebounceProperty<T> : MutableProperty<T> {
    fun forceSet(value: T)
}

typealias Executor = (delay: Long, action: () -> Unit) -> Cancellable

typealias Cancellable = () -> Unit
