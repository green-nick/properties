package com.github.greennick.properties

import com.github.greennick.properties.generic.MutableProperty
import com.github.greennick.properties.generic.PropertyImpl
import com.github.greennick.properties.generic.TriggeredProperty

/**
 * Create Property object of given object.
 * You can read, write and listen it's changes.
 *
 * This checks every new assignment on equality and skips it if new value is the same as previous.
 */
fun <T> propertyOf(value: T): MutableProperty<T> = PropertyImpl(value)

/**
 * Create Property object of given object.
 * You can read, write and listen value's assignments.
 *
 * The only difference between this and [propertyOf] is
 * this property doesn't use equality checks at all, so it will be triggered on every new assignment.
 *
 * There are two purposes of using this property:
 * 1. You need to be triggered on every assignment;
 * 2. Value that has to be assigned has big computational load on equality checks.
 *    For example a list which contains a lot of elements inside
 */
fun <T> triggerPropertyOf(value: T): MutableProperty<T> = TriggeredProperty(value)

/**
 * Alias to [propertyOf] that allows to skip setting initial value, but it will set nullable type.
 */
fun <T> emptyProperty(value: T? = null): MutableProperty<T?> = propertyOf(value)