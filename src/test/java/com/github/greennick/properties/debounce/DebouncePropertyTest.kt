package com.github.greennick.properties.debounce

import com.github.greennick.properties.debouncePropertyOf
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

class DebouncePropertyTest {
    private val executor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
    private val now get() = System.currentTimeMillis()
    private val delay = 100L

    @Test
    fun `first update made after exact delay`() {
        val startTime = now
        val countdown = CountDownLatch(2)

        val property = debouncePropertyOf("hello", delay, executor)
        property.subscribe {
            println("[$it] in thread ${Thread.currentThread().name}")

            countdown.countDown()
        }

        property.value = "world"
        countdown.await()
        assert(now - startTime >= delay)
    }

    @Test
    fun `get only last item after frequent updates during delay`() {
        val countdown = CountDownLatch(2)
        val collected = mutableListOf<String>()

        val property = debouncePropertyOf("a", delay, executor)
        property.subscribe {
            println("[$it] in thread ${Thread.currentThread().name}")
            collected += it
            countdown.countDown()
        }

        val lastValue = "dratuti"

        property.value = "b"
        property.value = "d"
        property.value = "c"
        property.value = lastValue

        countdown.await()

        println("collected: $collected")

        assert(collected.size == 2)
        assert(collected.last() == lastValue)
    }

    @Test
    fun `new item set after delay`() {
        val collected = mutableListOf<String>()

        val property = debouncePropertyOf("a", delay, executor)
        property.subscribe {
            println("[$it] in thread ${Thread.currentThread().name}")
            collected += it
        }

        val lastValue = "dratuti"
        val newValue = "hello"

        property.value = lastValue
        Thread.sleep(delay + 10)
        property.value = newValue
        Thread.sleep(delay + 10)


        println("collected: ${collected.size}")

        assert(collected.size == 3)
        assert(collected[1] == lastValue)
        assert(collected[2] == newValue)
    }

    @Test
    fun `coroutine first update made after exact delay`() = runBlocking {
        val property = debouncePropertyOf("hello", delay)
        property.subscribe {
            println("[$it] in thread ${Thread.currentThread().name}")
        }

        property.value = "world"
        assert(property.value != "world")
        kotlinx.coroutines.delay(delay + 10)
        assert(property.value == "world")
    }

    @Test
    fun `coroutine get only last item after frequent updates during delay`() = runBlocking {
        val collected = mutableListOf<String>()

        val property = debouncePropertyOf("a", delay, executor)
        property.subscribe {
            println("[$it] in thread ${Thread.currentThread().name}")
            collected += it
        }

        val lastValue = "dratuti"

        property.value = "b"
        property.value = "d"
        property.value = "c"
        property.value = lastValue

        kotlinx.coroutines.delay(delay + 10)

        println("collected: $collected")

        assert(collected.size == 2)
        assert(collected.last() == lastValue)
    }

    @Test
    fun `coroutine new item set after delay`() = runBlocking {
        val collected = mutableListOf<String>()

        val property = debouncePropertyOf("a", delay, executor)
        property.subscribe {
            println("[$it] in thread ${Thread.currentThread().name}")
            collected += it
        }

        val lastValue = "dratuti"
        val newValue = "hello"

        property.value = lastValue
        kotlinx.coroutines.delay(delay + 10)
        property.value = newValue
        kotlinx.coroutines.delay(delay + 10)

        println("collected: ${collected.size}")

        assert(collected.size == 3)
        assert(collected[1] == lastValue)
        assert(collected[2] == newValue)
    }
}
