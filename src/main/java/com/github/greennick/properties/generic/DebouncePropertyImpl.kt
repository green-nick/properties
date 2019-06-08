package com.github.greennick.properties.generic

import com.github.greennick.properties.Cancellable
import com.github.greennick.properties.Executor
import com.github.greennick.properties.subscriptions.ListenableSubscription

internal class DebounceProperty<T>(
    private val delay: Long,
    private val executor: Executor,
    initValue: T
) : MutableProperty<T> {
    private val listeners = linkedSetOf<(T) -> Unit>()
    private var previousUpdate: Cancellable? = null

    override var value = initValue
        set(value) {
            previousUpdate?.invoke()
            previousUpdate = executor.invoke(delay) {
                if (value == field) return@invoke
                field = value
                listeners.forEach { it(value) }
            }
        }

    override fun subscribe(onChanged: (T) -> Unit): ListenableSubscription {
        listeners += onChanged
        onChanged(value)
        return SubscriptionImpl(this, onChanged)
    }

    override fun toString() = "Property of [$value]"

    private class SubscriptionImpl<T>(
        private val propertyImpl: DebounceProperty<T>,
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