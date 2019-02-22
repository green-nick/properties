package com.github.greennick.properties.primitives.ints

import com.github.greennick.properties.ListenableSubscription

interface IntProperty {
    val value: Int

    fun subscribe(onChanged: (Int) -> Unit): ListenableSubscription
}

interface MutableIntProperty : IntProperty {
    override var value: Int
}