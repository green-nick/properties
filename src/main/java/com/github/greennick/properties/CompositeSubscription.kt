@file:Suppress("unused")

package com.github.greennick.properties

class CompositeSubscription : Subscription {
    private val subscriptions = mutableListOf<Subscription>()
    private var _subscribed = true

    override val subscribed: Boolean get() = _subscribed

    override fun unsubscribe() {
        if (!_subscribed) return

        _subscribed = false
        clear()
    }

    fun clear() {
        val iterator = subscriptions.iterator()
        while (iterator.hasNext()) {
            val subscription = iterator.next()
            subscription.unsubscribe()
            iterator.remove()
        }
    }

    fun add(vararg subscriptions: Subscription) {
        this.subscriptions.addAll(subscriptions)
    }
}

fun Subscription.attachTo(compositeSubscription: CompositeSubscription) {
    compositeSubscription.add(this)
}