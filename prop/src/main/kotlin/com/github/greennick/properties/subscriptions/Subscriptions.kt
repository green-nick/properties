package com.github.greennick.properties.subscriptions

interface Subscription {
    val subscribed: Boolean

    fun unsubscribe()
}

interface ListenableSubscription : Subscription {
    fun onUnsubscribe(action: () -> Unit)
}