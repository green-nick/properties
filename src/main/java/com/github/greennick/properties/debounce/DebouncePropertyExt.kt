package com.github.greennick.properties.debounce

import java.util.concurrent.Future
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

internal class JavaExecutor(private val executor: ScheduledExecutorService) : Executor {

    override fun invoke(delay: Long, action: () -> Unit): Cancellable =
        FutureWrapper(executor.schedule(action, delay, TimeUnit.MILLISECONDS))
}

private class FutureWrapper(private val future: Future<*>) : Cancellable {

    override fun invoke() {
        future.cancel(false)
    }
}
