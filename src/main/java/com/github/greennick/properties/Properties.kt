package com.github.greennick.properties

interface Property<T> {
    val value: T

    fun subscribe(onChanged: (T) -> Unit): ListenableSubscription
}

interface MutableProperty<T> : Property<T> {
    override var value: T
}