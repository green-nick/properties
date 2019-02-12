package com.github.greennick.properties

internal class PropertyImpl<T>(private var value: T) : MutableProperty<T> {
    private val listeners = linkedSetOf<(T) -> Unit>()

    override fun get() = value

    override fun subscribe(onChanged: (T) -> Unit): ListenableSubscription {
        listeners += onChanged
        onChanged(value)
        return SubscriptionImpl(this, onChanged)
    }

    override fun set(new: T) {
        if (new == value) return
        value = new
        listeners.forEach { it(value) }
    }

    override fun toString() = "Property of [$value]"

    private class SubscriptionImpl<T>(
        private val propertyImpl: PropertyImpl<T>,
        private val action: (T) -> Unit
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