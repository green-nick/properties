package com.github.greennick.properties.primitives.longs

import com.github.greennick.properties.subscriptions.ListenableSubscription

internal class LongPropertyImpl(initValue: Long) : MutableLongProperty {
    private val listeners = linkedSetOf<(Long) -> Unit>()

    override var value: Long = initValue
        set(value) {
            if (value == field) return
            field = value
            listeners.forEach { it(value) }
        }

    override fun subscribe(onChanged: (Long) -> Unit): ListenableSubscription {
        listeners += onChanged
        onChanged(value)
        return SubscriptionImpl(this, onChanged)
    }

    override fun toString() = "Property of [$value]"

    private class SubscriptionImpl(
        private val propertyImpl: LongPropertyImpl,
        private val action: (Long) -> Unit
    ) : ListenableSubscription {
        private var onUnsubscribe: (() -> Unit)? = null

        override val subscribed: Boolean
            get() {
                return action in propertyImpl.listeners
            }

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