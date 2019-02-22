package com.github.greennick.properties

import com.github.greennick.properties.generic.MutableProperty
import com.github.greennick.properties.generic.PropertyImpl
import com.github.greennick.properties.primitives.booleans.BooleanPropertyImpl
import com.github.greennick.properties.primitives.booleans.MutableBooleanProperty
import com.github.greennick.properties.primitives.doubles.DoublePropertyImpl
import com.github.greennick.properties.primitives.doubles.MutableDoubleProperty
import com.github.greennick.properties.primitives.ints.IntPropertyImpl
import com.github.greennick.properties.primitives.ints.MutableIntProperty

fun <T> propertyOf(value: T): MutableProperty<T> = PropertyImpl(value)

fun <T> emptyProperty(): MutableProperty<T?> = PropertyImpl(null)

fun propertyOf(value: Int): MutableIntProperty = IntPropertyImpl(value)

fun propertyOf(value: Double): MutableDoubleProperty = DoublePropertyImpl(value)

fun propertyOf(value: Boolean): MutableBooleanProperty = BooleanPropertyImpl(value)