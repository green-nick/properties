package com.github.greennick.properties.memoize

import com.github.greennick.properties.generic.MutableProperty

class MemoizePropertyImpl<T, M : MutableProperty<T>>(override val origin: M) : MemoizeProperty<T, M> {

    private val history = mutableListOf(origin.value)
    private var onNewSet: () -> Unit = {}

    override val size = history.size

    override var position: Int = 0
        set(value) {
            if (value < 0 || value >= size) {
                throw IllegalStateException("Position should be in range 0..size")
            } else if (value == field) {
                return
            }
            origin.value = history[value]
            field = value
        }

    override var value: T
        get() = origin.value
        set(value) {
            history.add(value)
            position = size - 1
            onNewSet()
        }

    override fun subscribe(onChanged: (T) -> Unit) = origin.subscribe(onChanged)

    internal fun onNewValueSet(onSet: () -> Unit) {
        onNewSet = onSet
    }
}
