package com.github.greennick.properties

fun <T> propertyOf(value: T): MutableProperty<T> = PropertyImpl(value)

fun <T> emptyProperty(): MutableProperty<T?> = PropertyImpl(null)