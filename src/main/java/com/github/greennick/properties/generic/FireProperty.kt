package com.github.greennick.properties.generic

import com.github.greennick.properties.subscriptions.ListenableSubscription

internal class FireProperty<T>(initValue: T) : MutableProperty<T> {
    private val listeners = linkedSetOf<(T) -> Unit>()
    private var consumed = false

    override var value = initValue
        set(value) {
            if (value == field) return
            field = value
            consumed = false
            listeners.forEach { it(value) }
        }

    override fun subscribe(onChanged: (T) -> Unit): ListenableSubscription {
        listeners += onChanged

        if (!consumed) {
            onChanged(value)
            consumed = true
        }
        return SubscriptionImpl(this, onChanged)
    }

    override fun toString() = "Fire property of [$value]"

    private class SubscriptionImpl<T>(
        private val propertyImpl: FireProperty<T>,
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