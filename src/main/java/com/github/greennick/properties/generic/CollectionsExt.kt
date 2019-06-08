package com.github.greennick.properties.generic

val list = listOf<String>()

val map = mapOf<String, String>()

val set = setOf<String>()

/**
 * Collection extensions
 */

fun <T> Property<Collection<T>>.size(): Int = value.size

fun <T> Property<Collection<T>>.isEmpty(): Boolean = value.isEmpty()

operator fun <T> Property<Collection<T>>.contains(element: T): Boolean = element in value

/**
 * List extensions
 */

operator fun <T> MutableProperty<List<T>>.plusAssign(element: T) {
    value = value + element
}

operator fun <T> Property<List<T>>.get(index: Int): T = value[index]

/**
 * Map extensions
 */

operator fun <K, V> MutableProperty<Map<K, V>>.plusAssign(entry: Pair<K, V>) {
    value = value + entry
}

operator fun <K, V> Property<Map<K, V>>.get(key: K): V? = value[key]
