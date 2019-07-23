package com.github.greennick.properties.generic

import com.github.greennick.properties.subscriptions.ListenableSubscription

interface Property<out T> {
    val value: T

    fun subscribe(onChanged: (T) -> Unit): ListenableSubscription
}

interface MutableProperty<T> : Property<T> {
    override var value: T
}
