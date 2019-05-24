package com.github.greennick.properties.subscriptions

class CompositeSubscription : Subscription {
    private val subscriptions = mutableListOf<Subscription>()
    private var _subscribed = true

    override val subscribed get() = _subscribed

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

    operator fun plusAssign(subscription: Subscription): Unit = add(subscription)
}

fun Subscription.addTo(compositeSubscription: CompositeSubscription): Unit = compositeSubscription.add(this)