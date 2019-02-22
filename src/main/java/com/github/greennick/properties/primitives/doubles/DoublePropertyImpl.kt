package com.github.greennick.properties.primitives.doubles

import com.github.greennick.properties.subscriptions.ListenableSubscription

class DoublePropertyImpl(initValue: Double): MutableDoubleProperty {
    private val listeners = linkedSetOf<(Double) -> Unit>()

    override var value: Double = initValue
        set(value) {
            if (value == field) return
            field = value
            listeners.forEach { it(value) }
        }

    override fun subscribe(onChanged: (Double) -> Unit): ListenableSubscription {
        listeners += onChanged
        onChanged(value)
        return SubscriptionImpl(this, onChanged)
    }

    private class SubscriptionImpl(
        private val propertyImpl: DoublePropertyImpl,
        private val action: (Double) -> Unit
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