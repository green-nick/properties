@file:Suppress("unused")

package com.github.greennick.properties

interface Property<T> {
    fun get(): T

    fun subscribe(onChanged: (T) -> Unit): ListenableSubscription
}

interface MutableProperty<T> : Property<T> {
    fun set(new: T)
}

fun <T> propertyOf(value: T): MutableProperty<T> = PropertyImpl(value)

fun <T> emptyProperty(): MutableProperty<T?> = PropertyImpl(null)