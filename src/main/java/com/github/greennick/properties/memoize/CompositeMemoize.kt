package com.github.greennick.properties.memoize

class CompositeMemoize(private vararg val memos: MemoizeProperty<*, *>) {

    private val history = mutableListOf<Pair<MemoizeProperty<*, *>, Int>>()
    private val initPositions = memos.map { it.position }

    val size: Int get() = history.size + 1

    var position: Int = 0
        set(value) {
            when {
                value < 0 || value >= size -> throw IllegalStateException("Position should be in range 0..size")
                value == field -> return
                value == 0 -> memos.forEachIndexed { index, memo ->
                    memo.position = initPositions[index]
                }
                value == 1 -> field = value
                field < value -> goToNewer(current = field, new = value)
                else -> goToOlder(current = field, old = value)
            }

            field = value
        }

    init {
        memos.filterIsInstance(MemoizePropertyImpl::class.java)
            .forEach { memo ->
                memo.onNewValueSet {
                    if (position < size - 1) {
                        clearHistory()
                    }

                    history.add(memo to memo.position)
                    position = size - 1
                }
            }
    }

    private fun goToNewer(current: Int, new: Int) {
        history.subList(current - 1, new - 1)
            .forEach { (property, position) -> property.position = position }
    }

    private fun goToOlder(current: Int, old: Int) {
        history.subList(old - 1, current - 1)
            .reversed()
            .forEach { (property, position) -> property.position = position }
    }

    private fun clearHistory() {
        ((size - 2) downTo position).forEach {
            history.removeAt(it)
        }
    }

    override fun toString() =
        "CompositeMemoize [${position + 1}/$size]:\n" +
                (listOf(
                    memos.mapIndexed { index, memo ->
                        "${memo.origin}:${initPositions[index]}"
                    }.joinToString()
                ) + history.map { (memo, position) ->
                    "${memo.origin}:$position"
                }).mapIndexed { index, string ->
                    if (index == position) {
                        "* "
                    } else {
                        "  "
                    } + string
                }.joinToString("\n")

}
