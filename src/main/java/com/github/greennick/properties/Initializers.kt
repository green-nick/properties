package com.github.greennick.properties

import com.github.greennick.properties.generic.MutableProperty
import com.github.greennick.properties.generic.PropertyImpl

fun <T> propertyOf(value: T): MutableProperty<T> = PropertyImpl(value)

fun <T> emptyProperty(value: T? = null): MutableProperty<T?> = PropertyImpl(value)