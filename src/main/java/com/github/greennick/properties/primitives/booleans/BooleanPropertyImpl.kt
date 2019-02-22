package com.github.greennick.properties.primitives.booleans

import com.github.greennick.properties.subscriptions.ListenableSubscription

internal class BooleanPropertyImpl(initValue: Boolean) : MutableBooleanProperty {
    private val listeners = linkedSetOf<(Boolean) -> Unit>()

    override var value = initValue
        set(value) {
            if (value == field) return
            field = value
            listeners.forEach { it(value) }
        }

    override fun subscribe(onChanged: (Boolean) -> Unit): ListenableSubscription {
        listeners += onChanged
        onChanged(value)
        return SubscriptionImpl(this, onChanged)
    }

    override fun toString() = "Property of [$value]"

    private class SubscriptionImpl(
        private val propertyImpl: BooleanPropertyImpl,
        private val action: (Boolean) -> Unit
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