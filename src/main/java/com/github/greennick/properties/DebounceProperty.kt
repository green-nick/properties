package com.github.greennick.properties

import com.github.greennick.properties.generic.DebounceProperty
import com.github.greennick.properties.generic.MutableProperty
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

fun <T> debouncePropertyOf(value: T, delay: Long, executor: Executor = JavaExecutor()): MutableProperty<T> =
    DebounceProperty(delay, executor, value)

typealias Executor = (delay: Long, action: () -> Unit) -> Cancellable

typealias Cancellable = () -> Unit

class JavaExecutor(
    private val executor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
) : Executor {

    override fun invoke(delay: Long, action: () -> Unit): Cancellable =
        FutureWrapper(executor.schedule(action, delay, TimeUnit.MILLISECONDS))
}

class FutureWrapper(private val future: Future<*>) : Cancellable {

    override fun invoke() {
        future.cancel(false)
    }
}