@file:Suppress("unused")

package com.github.greennick.properties

interface Property<T> {
    val value: T

    fun subscribe(onChanged: (T) -> Unit): ListenableSubscription
}

interface MutableProperty<T> : Property<T> {
    override var value: T
}

fun <T> propertyOf(value: T): MutableProperty<T> = PropertyImpl(value)

fun <T> emptyProperty(): MutableProperty<T?> = PropertyImpl(null)