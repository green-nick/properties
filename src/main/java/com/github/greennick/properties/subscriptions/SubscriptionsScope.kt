package com.github.greennick.properties.subscriptions

import com.github.greennick.properties.generic.Property

interface SubscriptionsScope {
    val subscriptions: CompositeSubscription

    fun <T> Property<T>.subscribeScoped(onChanged: (T) -> Unit) = subscribe(onChanged).also(subscriptions::plusAssign)
}