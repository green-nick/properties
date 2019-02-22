package com.github.greennick.properties.primitives.doubles

import com.github.greennick.properties.subscriptions.ListenableSubscription

interface DoubleProperty {
    val value: Double

    fun subscribe(onChanged: (Double) -> Unit): ListenableSubscription
}

interface MutableDoubleProperty : DoubleProperty {
    override var value: Double
}