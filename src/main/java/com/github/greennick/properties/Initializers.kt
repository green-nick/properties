package com.github.greennick.properties

import com.github.greennick.properties.primitives.ints.IntPropertyImpl
import com.github.greennick.properties.primitives.ints.MutableIntProperty

fun <T> propertyOf(value: T): MutableProperty<T> = PropertyImpl(value)

fun <T> emptyProperty(): MutableProperty<T?> = PropertyImpl(null)

fun propertyOf(value: Int): MutableIntProperty = IntPropertyImpl(value)