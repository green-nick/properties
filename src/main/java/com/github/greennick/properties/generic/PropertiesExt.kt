package com.github.greennick.properties.generic

import com.github.greennick.properties.propertyOf

/**
 * General extensions section
 */
operator fun <T> Property<T>.invoke(onChanged: (T) -> Unit) = subscribe(onChanged)

/**
 * Combining extensions section
 */

operator fun <T, R> Property<T>.plus(another: Property<R>): MutableProperty<Pair<T, R>> {
    val new = propertyOf(this.value to another.value)

    this.subscribe { new.value = it to another.value }
    another.subscribe { new.value = this.value to it }

    return new
}

fun <T, R> Property<T>.map(mapper: (T) -> R): MutableProperty<R> {
    val new = propertyOf(mapper(this.value))

    this.subscribe { new.value = mapper(it) }

    return new
}

fun <T, R, E> Property<T>.zipWith(another: Property<R>, zipper: (T, R) -> E): MutableProperty<E> {
    val new = propertyOf(zipper(this.value, another.value))

    this.subscribe { new.value = zipper(it, another.value) }
    another.subscribe { new.value = zipper(this.value, it) }

    return new
}

/**
 * Boolean extensions section
 */

operator fun Property<Boolean>.not(): MutableProperty<Boolean> {
    val new = propertyOf(!this.value)

    this.subscribe { new.value = !it }

    return new
}

fun MutableProperty<Boolean>.toggle() {
    this.value = !this.value
}