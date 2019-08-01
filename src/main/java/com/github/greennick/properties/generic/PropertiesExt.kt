package com.github.greennick.properties.generic

import com.github.greennick.properties.debounce.DebounceProperty
import com.github.greennick.properties.memoize.MemoizeProperty
import com.github.greennick.properties.memoize.MemoizePropertyImpl
import com.github.greennick.properties.propertyOf
import com.github.greennick.properties.subscriptions.ListenableSubscription

/**
 * General extensions section
 */
operator fun <T> Property<T>.invoke(onChanged: (T) -> Unit): ListenableSubscription =
    subscribe(onChanged)

fun <T> MutableProperty<T?>.reset() {
    value = null
}

fun <T> Property<T?>.subscribeNonNull(onChanged: (T) -> Unit): ListenableSubscription =
    subscribe { it?.also(onChanged) }

val <T> MutableProperty<T>.memoized: MemoizeProperty<T, MutableProperty<T>>
    get() = MemoizePropertyImpl(this)

val <T> DebounceProperty<T>.memoized: MemoizeProperty<T, DebounceProperty<T>>
    get() = MemoizePropertyImpl(this)

fun MemoizeProperty<*, *>.first() {
    position = 0
}

fun MemoizeProperty<*, *>.last() {
    position = size - 1
}

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

fun <T1, T2, E> Property<T1>.zipWith(another: Property<T2>, zipper: (T1, T2) -> E): MutableProperty<E> {
    val new = propertyOf(zipper(this.value, another.value))

    this.subscribe { new.value = zipper(it, another.value) }
    another.subscribe { new.value = zipper(this.value, it) }

    return new
}

fun <T1, T2, T3, R> Property<T1>.zipWith(
    second: Property<T2>,
    third: Property<T3>,
    zipper: (T1, T2, T3) -> R
): MutableProperty<R> {
    val new = propertyOf(zipper(this.value, second.value, third.value))

    this.subscribe { new.value = zipper(it, second.value, third.value) }
    second.subscribe { new.value = zipper(this.value, it, third.value) }
    third.subscribe { new.value = zipper(this.value, second.value, it) }

    return new
}

fun <T1, T2, T3, T4, R> Property<T1>.zipWith(
    second: Property<T2>,
    third: Property<T3>,
    fourth: Property<T4>,
    zipper: (T1, T2, T3, T4) -> R
): MutableProperty<R> {
    val new = propertyOf(zipper(this.value, second.value, third.value, fourth.value))

    this.subscribe { new.value = zipper(it, second.value, third.value, fourth.value) }
    second.subscribe { new.value = zipper(this.value, it, third.value, fourth.value) }
    third.subscribe { new.value = zipper(this.value, second.value, it, fourth.value) }
    fourth.subscribe { new.value = zipper(this.value, second.value, third.value, it) }

    return new
}

fun <T1, T2, T3, T4, T5, R> Property<T1>.zipWith(
    second: Property<T2>,
    third: Property<T3>,
    fourth: Property<T4>,
    fifth: Property<T5>,
    zipper: (T1, T2, T3, T4, T5) -> R
): MutableProperty<R> {
    val new = propertyOf(zipper(this.value, second.value, third.value, fourth.value, fifth.value))

    this.subscribe { new.value = zipper(it, second.value, third.value, fourth.value, fifth.value) }
    second.subscribe { new.value = zipper(this.value, it, third.value, fourth.value, fifth.value) }
    third.subscribe { new.value = zipper(this.value, second.value, it, fourth.value, fifth.value) }
    fourth.subscribe { new.value = zipper(this.value, second.value, third.value, it, fifth.value) }
    fifth.subscribe { new.value = zipper(this.value, second.value, third.value, fourth.value, it) }

    return new
}

/**
 * Boolean extensions section
 */

fun Property<Boolean?>.subscribeOnTrue(onTrue: () -> Unit): ListenableSubscription =
    subscribe {
        if (it == true) onTrue()
    }

fun Property<Boolean?>.subscribeOnFalse(onFalse: () -> Unit): ListenableSubscription =
    subscribe {
        if (it == false) onFalse()
    }

operator fun Property<Boolean>.not(): MutableProperty<Boolean> {
    val new = propertyOf(!this.value)

    this.subscribe { new.value = !it }

    return new
}

fun MutableProperty<Boolean>.toggle() {
    this.value = !this.value
}
