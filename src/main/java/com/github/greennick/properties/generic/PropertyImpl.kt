package com.github.greennick.properties.generic

import com.github.greennick.properties.subscriptions.ListenableSubscription
import com.github.greennick.properties.subscriptions.SubscriptionImpl
import java.util.concurrent.CopyOnWriteArraySet

internal class PropertyImpl<T>(initValue: T) : MutableProperty<T> {
    private val listeners = CopyOnWriteArraySet<(T) -> Unit>()

    override var value = initValue
        set(value) {
            if (value == field) return
            field = value
            listeners.forEach { it(value) }
        }

    override fun subscribe(onChanged: (T) -> Unit): ListenableSubscription {
        listeners += onChanged
        onChanged(value)
        return SubscriptionImpl(listeners, onChanged)
    }

    override fun toString() = "Property of [$value]"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PropertyImpl<*>

        if (value != other.value) return false

        return true
    }

    override fun hashCode() = value.hashCode()
}
