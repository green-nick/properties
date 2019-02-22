package com.github.greennick.properties.primitives.longs

import com.github.greennick.properties.subscriptions.ListenableSubscription

interface LongProperty {
    val value: Long

    fun subscribe(onChanged: (Long) -> Unit): ListenableSubscription
}

interface MutableLongProperty : LongProperty {
    override var value: Long
}