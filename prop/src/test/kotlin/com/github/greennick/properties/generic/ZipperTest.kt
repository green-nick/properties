package com.github.greennick.properties.generic

import com.github.greennick.properties.propertyOf
import org.junit.Test

class ZipperTest {

    @Test
    fun `zipped property inherit value`() {
        val zipper = { hi: String, person: String -> "$hi, $person!" }
        val initHi = "Hello"
        val initPerson = "world"

        val hi = propertyOf(initHi)
        val person = propertyOf(initPerson)

        val new = hi.zipWith(person, zipper)

        assert(new.value == zipper(initHi, initPerson))
    }
}