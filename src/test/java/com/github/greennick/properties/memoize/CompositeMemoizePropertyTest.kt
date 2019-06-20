package com.github.greennick.properties.memoize

import com.github.greennick.properties.generic.memoized
import com.github.greennick.properties.propertyOf
import org.junit.Test

class CompositeMemoizePropertyTest {

    @Test
    fun `to string`() {
        val prop1 = propertyOf("hello").memoized
        val prop2 = propertyOf("dratuti").memoized
        val prop3 = propertyOf(1).memoized
        val prop4 = propertyOf(20.0).memoized

        val composite = CompositeMemoize(prop1, prop2, prop3, prop4)
        prop2.value = "aloha"
        prop3.value = 7
        prop3.value = 8
        prop4.value = 0.0

//        composite.position = 2

        println(composite)
    }
}
