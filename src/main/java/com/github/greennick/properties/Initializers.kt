package com.github.greennick.properties

import com.github.greennick.properties.debounce.*
import com.github.greennick.properties.debounce.DebouncePropertyImpl
import com.github.greennick.properties.generic.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.ScheduledExecutorService
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Create Property object of given object.
 * You can read, write and listen it's changes.
 *
 * This checks every new assignment on equality and skips it if new value is the same as previous.
 */
fun <T> propertyOf(value: T): MutableProperty<T> = PropertyImpl(value)

/**
 * Alias to [propertyOf] that allows to skip setting initial value, but it will set nullable type.
 */
fun <T> emptyProperty(value: T? = null): MutableProperty<T?> = propertyOf(value)

/**
 * Create Property object of given object.
 * You can read, write and listen value's assignments.
 *
 * The only difference between this and [propertyOf] is
 * this property doesn't use equality checks at all, so it will be triggered on every new assignment.
 */
fun <T> triggerPropertyOf(value: T): MutableProperty<T> = TriggeredProperty(value)

/**
 * Special property that emits value only one time.
 * If there is new subscription, it won't receive updates until new assignment will be done.
 *
 * Pay attention, that there is only one active subscriber exist.
 * Every new subscription will cancel previous one automatically.
 */
fun <T> firePropertyOf(value: T): MutableProperty<T> = FireProperty(value)

/**
 * Only set an item to Property if a particular delay has passed without it setting another item.
 *
 * @param delay - threshold which has to be passed, before new value will be set.
 * @param executor - executor which able to postpone task execution with given delay and supports its cancellation.
 */
fun <T> debouncePropertyOf(value: T, delay: Long, executor: Executor): DebounceProperty<T> =
    DebouncePropertyImpl(delay, executor, value)

/**
 * Only set an item to Property if a particular delay has passed without it setting another item.
 *
 * @param delay - threshold which has to be passed, before new value will be set.
 * @param context - Updates execution context e.g. some Dispatcher. Default one is [EmptyCoroutineContext]
 */
fun <T> CoroutineScope.debouncePropertyOf(
    value: T,
    delay: Long,
    context: CoroutineContext = EmptyCoroutineContext
): DebounceProperty<T> {
    val executor: Executor = { delayMillis, action ->
        val job = launch(context) {
            kotlinx.coroutines.delay(delayMillis)
            action()
        }
        val cancellable: Cancellable = { job.cancel() }
        cancellable
    }
    return debouncePropertyOf(value, delay, executor)
}

/**
 * Only set an item to Property if a particular delay has passed without it setting another item.
 * Based on Java's ScheduledExecutorService
 *
 * @param delay - threshold which has to be passed, before new value will be set.
 * @param executor - ScheduledExecutorService will handle updates scheduling.
 */
fun <T> debouncePropertyOf(value: T, delay: Long, executor: ScheduledExecutorService): DebounceProperty<T> =
    debouncePropertyOf(value, delay, JavaExecutor(executor))
