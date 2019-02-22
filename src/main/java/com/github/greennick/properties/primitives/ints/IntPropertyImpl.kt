package com.github.greennick.properties.primitives.ints

import com.github.greennick.properties.subscriptions.ListenableSubscription

internal class IntPropertyImpl(initValue: Int): MutableIntProperty {
    private val listeners = linkedSetOf<(Int) -> Unit>()

    override var value: Int = initValue
        set(value) {
            if (value == field) return
            field = value
            listeners.forEach { it(value) }
        }

    override fun subscribe(onChanged: (Int) -> Unit): ListenableSubscription {
        listeners += onChanged
        onChanged(value)
        return SubscriptionImpl(this, onChanged)
    }


    private class SubscriptionImpl(
        private val propertyImpl: IntPropertyImpl,
        private val action: (Int) -> Unit
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