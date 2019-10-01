package com.github.greennick.properties.subscriptions

import java.util.concurrent.CopyOnWriteArrayList

class CompositeSubscription : Subscription {
    private val subscriptions = CopyOnWriteArrayList<Subscription>()
    private var _subscribed = true

    override val subscribed get() = _subscribed

    override fun unsubscribe() {
        if (!_subscribed) return

        _subscribed = false
        clear()
    }

    fun clear() {
        subscriptions.forEach(Subscription::unsubscribe)
        subscriptions.clear()
    }

    fun add(vararg subscriptions: Subscription) {
        this.subscriptions.addAll(subscriptions)
    }

    operator fun plusAssign(subscription: Subscription): Unit = add(subscription)
}

fun Subscription.addTo(compositeSubscription: CompositeSubscription): Unit = compositeSubscription.add(this)