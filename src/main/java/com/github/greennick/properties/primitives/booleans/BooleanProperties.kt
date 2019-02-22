package com.github.greennick.properties.primitives.booleans

import com.github.greennick.properties.subscriptions.ListenableSubscription

interface BooleanProperty {
    val value: Boolean

    fun subscribe(onChanged: (Boolean) -> Unit): ListenableSubscription
}

interface MutableBooleanProperty : BooleanProperty {
    override var value: Boolean
}