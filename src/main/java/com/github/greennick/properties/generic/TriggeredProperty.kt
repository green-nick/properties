package com.github.greennick.properties.generic

import com.github.greennick.properties.subscriptions.ListenableSubscription

internal class TriggeredProperty<T>(initValue: T) : MutableProperty<T> {
    private val listeners = linkedSetOf<(T) -> Unit>()

    override var value = initValue
        set(value) {
            field = value
            listeners.forEach { it(value) }
        }

    override fun subscribe(onChanged: (T) -> Unit): ListenableSubscription {
        listeners += onChanged
        onChanged(value)
        return SubscriptionImpl(this, onChanged)
    }

    override fun toString() = "Property of [$value]"

    private class SubscriptionImpl<T>(
        private val propertyImpl: TriggeredProperty<T>,
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