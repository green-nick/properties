package com.github.greennick.properties.generic

import com.github.greennick.properties.subscriptions.ListenableSubscription
import java.util.concurrent.CopyOnWriteArraySet

internal class ThreadSafeProperty<T>(initValue: T) : MutableProperty<T> {
    private val listeners = CopyOnWriteArraySet<(T) -> Unit>()

    @Volatile
    override var value = initValue
        set(value) {
            synchronized(this) {
                if (value == field) return
                field = value
            }
            listeners.forEach { it(value) }
        }

    override fun subscribe(onChanged: (T) -> Unit): ListenableSubscription {
        listeners += onChanged
        onChanged(value)
        return SubscriptionImpl(this, onChanged)
    }

    override fun toString() = "Property of [$value]"

    private class SubscriptionImpl<T>(
        private val propertyImpl: ThreadSafeProperty<T>,
        private val action: (T) -> Unit
    ) : ListenableSubscription {
        @Volatile
        private var onUnsubscribe: (() -> Unit)? = null

        override val subscribed get() = action in propertyImpl.listeners

        @Synchronized
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