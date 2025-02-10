package dev.bnorm.evolved.template.code

import androidx.compose.ui.text.AnnotatedString

// TODO should each token be able to also control things like delay?
//  really need context parameters for this to work seamlessly...
data class Token(val text: AnnotatedString, val key: Any?)

internal val tagRegex = "<(?<tag>/?[^<>]*)>".toRegex()

fun AnnotatedString.toTokens(): List<Token?> {
    var key = 0
    fun nextKey(): String = "s:${key++}"

    fun tokenize(annotatedString: AnnotatedString): List<Token> = buildList<Token> {
        var last: MatchResult? = null
        for (match in tagRegex.findAll(annotatedString)) {
            val startIndex = if (last == null) 0 else last.range.endInclusive + 1
            val subString = annotatedString.subSequence(startIndex, match.range.start)
            if (subString.text != "") {
                add(
                    when {
                        last == null -> Token(
                            text = subString,
                            key = nextKey(),
                        )

                        match.groupValues[1] == "/i" -> Token(
                            text = subString,
                            key = null,
                        )

                        match.groupValues[1].startsWith("/m=") -> Token(
                            text = subString,
                            key = "m:${match.groupValues[1].substring(startIndex = 3)}"
                        )

                        else -> Token(
                            text = subString,
                            key = nextKey(),
                        )
                    }
                )
            }

            last = match
        }

        val subString = when {
            last == null -> annotatedString
            else -> annotatedString.subSequence(last.range.endInclusive + 1, annotatedString.length)
        }
        if (subString.text != "") add(Token(text = subString, key = nextKey()))
    }

    return buildList {
        val lines = lines()
        for ((i, line) in lines.withIndex()) {
            if (i > 0) add(null)
            addAll(tokenize(line))
        }
    }
}

private fun AnnotatedString.lines(): List<AnnotatedString> = buildList {
    var index = 0
    while (true) {
        val next = indexOf('\n', startIndex = index)
        if (next == -1) {
            add(subSequence(index, length))
            break
        } else {
            add(subSequence(index, next))
            index = next + 1
        }
    }
}
