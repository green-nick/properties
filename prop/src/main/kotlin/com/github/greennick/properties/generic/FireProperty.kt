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
        return FireSubscriptionImpl(this, onChanged)
    }

    override fun toString() = "Fire property of [$value], consumed: $consumed"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FireProperty<*>

        if (consumed != other.consumed) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = consumed.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }

    private class FireSubscriptionImpl<T>(
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
