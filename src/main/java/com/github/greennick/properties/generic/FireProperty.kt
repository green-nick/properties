package com.github.greennick.properties.generic

import com.github.greennick.properties.subscriptions.ListenableSubscription

internal class FireProperty<T>(initValue: T) : MutableProperty<T> {
    private var listener: ((T) -> Unit)? = null
    private var consumed = false

    override var value = initValue
        set(value) {
            field = value
            consumed = false
            listener?.invoke(value)
        }

    override fun subscribe(onChanged: (T) -> Unit): ListenableSubscription {
        listener = onChanged

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

        override val subscribed get() = action == propertyImpl.listener

        override fun unsubscribe() {
            if (!subscribed) return

            propertyImpl.listener = null
            onUnsubscribe?.invoke()
        }

        override fun onUnsubscribe(action: () -> Unit) {
            onUnsubscribe = action
        }
    }
}