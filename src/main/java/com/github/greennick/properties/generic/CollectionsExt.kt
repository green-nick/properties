package com.github.greennick.properties.generic

/**
 * Collection extensions
 */

val <T> Property<out Collection<T>>.size: Int get() = value.size

fun <T> Property<out Collection<T>>.isEmpty(): Boolean = value.isEmpty()

fun <T> Property<out Collection<T>>.isNotEmpty(): Boolean = !isEmpty()

operator fun <T> Property<out Collection<T>>.contains(element: T): Boolean = element in value

operator fun <T> Property<out Collection<T>>.contains(another: Collection<T>): Boolean = value.containsAll(another)

/**
 * List extensions
 */

operator fun <T> MutableProperty<List<T>>.plusAssign(element: T) {
    value = value + element
}

operator fun <T> MutableProperty<List<T>>.plusAssign(another: Iterable<T>) {
    value = value + another
}

operator fun <T> MutableProperty<List<T>>.minusAssign(element: T) {
    value = value - element
}

operator fun <T> MutableProperty<List<T>>.minusAssign(another: Iterable<T>) {
    value = value - another
}

operator fun <T> Property<out List<T>>.get(index: Int): T = value[index]

/**
 * Map extensions
 */

operator fun <K, V> MutableProperty<Map<K, V>>.plusAssign(entry: Pair<K, V>) {
    value = value + entry
}

operator fun <K, V> MutableProperty<Map<K, V>>.plusAssign(another: Map<K, V>) {
    value = value + another
}

operator fun <K, V> Property<out Map<K, V>>.get(key: K): V? = value[key]
