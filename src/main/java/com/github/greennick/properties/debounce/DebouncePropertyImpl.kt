package com.github.greennick.properties.debounce

import com.github.greennick.properties.subscriptions.ListenableSubscription

internal class DebouncePropertyImpl<T>(
    private val delay: Long,
    private val executor: Executor,
    initValue: T
) : DebounceProperty<T> {
    private val listeners = linkedSetOf<(T) -> Unit>()
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
        return SubscriptionImpl(this, onChanged)
    }

    override fun forceSet(value: T) {
        previousUpdate?.invoke()
        if (value == _value) return
        _value = value
        listeners.forEach { it(value) }
    }

    override fun toString() = "Property of [$value]"

    private class SubscriptionImpl<T>(
        private val propertyImpl: DebouncePropertyImpl<T>,
        private val action: (T) -> Unit
    ) : ListenableSubscription {
        private var onUnsubscribe: (() -> Unit)? = null

        override val subscribed get() = action in propertyImpl.listeners

        override fun unsubscribe() {
            if (!subscribed) return

            if (propertyImpl.listeners.remove(action)) {
                onUnsubscribe?.invoke()
            }
        }

        override fun onUnsubscribe(action: () -> Unit) {
            onUnsubscribe = action
        }
    }
}