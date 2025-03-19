package dev.bnorm.kc25.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString

fun AnnotatedString.collapse(
    subStrings: List<String> = emptyList(),
    collapsed: SpanStyle = SpanStyle(color = Color.Companion.Gray.copy(alpha = 0.5f)),
): AnnotatedString {
    return collapseInternal(this, subStrings, collapsed)
}

private fun collapseInternal(
    string: AnnotatedString,
    subStrings: List<String>,
    collapsed: SpanStyle,
): AnnotatedString {
    if (subStrings.isEmpty()) return string

    val ellipsis = AnnotatedString("…", collapsed)
    return buildAnnotatedString {
        var last = 0
        while (true) {
            val (start, match) = string.findAnyOf(subStrings, startIndex = last) ?: break
            val end = start + match.length

            if (start != last) {
                append(string.subSequence(last, start))
            }

            append(ellipsis)

            last = end
        }

        if (last == 0) return string
        if (last != string.length) {
            append(string.subSequence(last, string.length))
        }
    }
}

fun AnnotatedString.collapse(
    names: Set<String>,
    collapsed: SpanStyle = SpanStyle(color = Color.Companion.Gray.copy(alpha = 0.5f)),
): AnnotatedString {
    return collapseInternal(this, names, collapsed)
}

private fun collapseInternal(
    string: AnnotatedString,
    names: Set<String>,
    collapsed: SpanStyle,
): AnnotatedString {
    val tokens = CollapseToken.findAll(string.text)

    val ranges = mutableListOf<Pair<IntRange, String>>()
    val starts = mutableListOf<CollapseToken.Start>()
    for (token in tokens) {
        when (token) {
            is CollapseToken.Start -> starts.add(token)
            is CollapseToken.End -> {
                val start = starts.removeLastOrNull()
                if (start == null) {
                    ranges.add(token.range to "")
                    continue
                }

                if (start.name in names) {
                    ranges.add(start.range.start..token.range.endInclusive to token.replacement)
                } else {
                    ranges.add(start.range to "")
                    ranges.add(token.range to "")
                }
            }
        }
    }

    for (token in starts) {
        ranges.add(token.range to "")
    }

    val builder = AnnotatedString.Builder()
    var last = -1
    for ((range, replacement) in ranges.sortedBy { it.first.start }) {
        if (range.start < last) continue

        if (range.start != last + 1) {
            builder.append(string.subSequence(last + 1, range.start))
        }
        if (replacement.isNotEmpty()) {
            builder.append(AnnotatedString(replacement, collapsed))
        }
        last = range.endInclusive
    }

    if (last == -1) return string
    if (last != string.length) {
        builder.append(string.subSequence(last + 1, string.length))
    }

    return builder.toAnnotatedString()
}

private sealed class CollapseToken {
    companion object {
        /*@collapse-start:main*/
        /*@collapse-end:...*/

        private val START = "/\\*@collapse-start:(?<name>.+?)\\*/".toRegex()
        private val END = "/\\*@collapse-end:(?<replacement>.*?)\\*/".toRegex()

        fun findAll(text: String): Sequence<CollapseToken> {
            val startMatches = START.findAll(text)
                .map { Start(it.groupValues[1], it.range) }

            val endMatches = END.findAll(text)
                .map { End(it.groupValues[1], it.range) }

            return (startMatches + endMatches).sortedBy { it.range.start }
        }
    }

    abstract val range: IntRange

    class Start(val name: String, override val range: IntRange) : CollapseToken()
    class End(val replacement: String, override val range: IntRange) : CollapseToken()
}
