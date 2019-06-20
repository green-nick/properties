package com.github.greennick.properties.memoize

import com.github.greennick.properties.generic.MutableProperty

interface MemoizeProperty<T, M : MutableProperty<T>> : MutableProperty<T> {

    val origin: M

    val size: Int

    var position: Int
}
