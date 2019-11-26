package com.github.greennick.properties.subscriptions

class SubscriptionImpl<T>(
    private val listeners: MutableCollection<(T) -> Unit>,
    private val action: (T) -> Unit
): ListenableSubscription {
    private var onUnsubscribe: (() -> Unit)? = null

    override val subscribed get() = action in listeners

    override fun unsubscribe() {
        if (!subscribed) return

        if (listeners.remove(action)) {
            onUnsubscribe?.invoke()
        }
    }

    override fun onUnsubscribe(action: () -> Unit) {
        onUnsubscribe = action
    }
}
