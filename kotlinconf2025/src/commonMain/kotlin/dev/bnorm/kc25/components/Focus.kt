package dev.bnorm.kc25.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString

fun AnnotatedString.focusOn(
    subStrings: List<String> = emptyList(),
    focused: SpanStyle = SpanStyle(),
    unfocused: SpanStyle = SpanStyle(color = Color.Companion.Gray.copy(alpha = 0.5f)),
): AnnotatedString {
    return focusOnInternal(this, subStrings, unfocused, focused)
}

private fun focusOnInternal(
    string: AnnotatedString,
    subStrings: List<String>,
    unfocused: SpanStyle,
    focused: SpanStyle,
): AnnotatedString {
    if (subStrings.isEmpty()) return string
    return buildAnnotatedString {
        append(string.text)

        var last = 0
        while (true) {
            val (start, match) = string.findAnyOf(subStrings, startIndex = last) ?: break
            val end = start + match.length

            if (start != last) {
                val styles = string.subSequence(last, start).spanStyles
                for (range in styles) {
                    addStyle(range.item + unfocused, last + range.start, last + range.end)
                }
                addStyle(unfocused, last, start)
            }

            for (range in string.subSequence(start, end).spanStyles) {
                addStyle(range.item + focused, start + range.start, start + range.end)
            }

            last = end
        }

        if (last == 0) return string
        if (last != string.length) {
            for (range in string.subSequence(last, string.length).spanStyles) {
                addStyle(range.item + unfocused, last + range.start, last + range.end)
            }
            addStyle(unfocused, last, string.length)
        }
    }
}
