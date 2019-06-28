package com.github.greennick.properties.memoize

import com.github.greennick.properties.generic.MutableProperty

internal class MemoizePropertyImpl<T, M : MutableProperty<T>>(override val origin: M) : MemoizeProperty<T, M> {

    private val _history = mutableListOf(origin.value)
    private var onNewSet: () -> Unit = {}

    override val size get() = _history.size
    override val history get() = _history.toList()

    override var position: Int = 0
        set(value) {
            if (value < 0 || value >= size) {
                throw IllegalStateException("Position should be in range 0..size")
            } else if (value == field) {
                return
            }
            origin.value = _history[value]
            field = value
        }

    override var value: T
        get() = origin.value
        set(value) {
            _history.add(value)
            position = size - 1
            onNewSet()
        }

    override fun subscribe(onChanged: (T) -> Unit) = origin.subscribe(onChanged)

    internal fun onNewValueSet(onSet: () -> Unit) {
        onNewSet = onSet
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MemoizePropertyImpl<*, *>

        if (origin != other.origin) return false
        if (_history != other._history) return false
        if (position != other.position) return false

        return true
    }

    override fun hashCode(): Int {
        var result = origin.hashCode()
        result = 31 * result + _history.hashCode()
        result = 31 * result + position
        return result
    }

    override fun toString(): String {
        return "MemoizeProperty(position=$position, origin=$origin, history=$_history)"
    }
}
