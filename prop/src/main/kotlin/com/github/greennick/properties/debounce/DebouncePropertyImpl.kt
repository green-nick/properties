package com.github.greennick.properties.debounce

import com.github.greennick.properties.subscriptions.ListenableSubscription
import com.github.greennick.properties.subscriptions.SubscriptionImpl
import java.util.concurrent.CopyOnWriteArraySet

internal class DebouncePropertyImpl<T>(
    private val delay: Long,
    private val executor: Executor,
    initValue: T
) : DebounceProperty<T> {
    private val listeners = CopyOnWriteArraySet<(T) -> Unit>()
    private var previousUpdate: Cancellable? = null

    @Volatile
    private var _value = initValue

    override var value
        get() = _value
        set(value) {
            previousUpdate?.invoke()
            previousUpdate = executor.invoke(delay) {
                if (value == _value) return@invoke
                _value = value
                listeners.forEach { it(value) }
            }
        }

    override fun subscribe(onChanged: (T) -> Unit): ListenableSubscription {
        listeners += onChanged
        onChanged(value)
        return SubscriptionImpl(listeners, onChanged)
    }

    override fun forceSet(value: T) {
        previousUpdate?.invoke()
        if (value == _value) return
        _value = value
        listeners.forEach { it(value) }
    }

    override fun toString() = "Debounce property of [$value] with $delay ms delay"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DebouncePropertyImpl<*>

        if (delay != other.delay) return false
        if (executor != other.executor) return false
        if (_value != other._value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = delay.hashCode()
        result = 31 * result + executor.hashCode()
        result = 31 * result + _value.hashCode()
        return result
    }
}
