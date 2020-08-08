package com.github.greennick.properties

import com.github.greennick.properties.generic.MutableProperty
import com.github.greennick.properties.subscriptions.ListenableSubscription

fun <T, M : MutableProperty<T>> M.testable(): TestableProperty<T, M> = TestableProperty(this)

class TestableProperty<T, M : MutableProperty<T>>(val origin: M) : MutableProperty<T> {
    var readCount = 0
        private set
    var writeCount = 0
        private set

    private val _changedHistory = mutableListOf<T>()
    val changedHistory: List<T> = _changedHistory

    val changedCount get() = changedHistory.size

    override var value: T
        get() {
            readCount++
            return origin.value
        }
        set(value) {
            origin.value = value
            writeCount++
        }

    override fun subscribe(onChanged: (T) -> Unit): ListenableSubscription {
        val wrapped: (T) -> Unit = {
            onChanged(it)
            _changedHistory.add(it)
        }
        return origin.subscribe(wrapped)
    }
}
