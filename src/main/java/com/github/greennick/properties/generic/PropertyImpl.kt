package com.github.greennick.properties.generic

import com.github.greennick.properties.subscriptions.ListenableSubscription

internal class PropertyImpl<T>(initValue: T) : MutableProperty<T> {
    private val listeners = linkedSetOf<(T) -> Unit>()

    override var value = initValue
        set(value) {
            if (value == field) return
            field = value
            listeners.forEach { it(value) }
        }

    override fun subscribe(onChanged: (T) -> Unit): ListenableSubscription {
        listeners += onChanged
        onChanged(value)
        return SubscriptionImpl(this, onChanged)
    }

    override fun toString() = "Property of [$value]"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PropertyImpl<*>

        if (value != other.value) return false

        return true
    }

    override fun hashCode() = value.hashCode()

    private class SubscriptionImpl<T>(
        private val propertyImpl: PropertyImpl<T>,
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