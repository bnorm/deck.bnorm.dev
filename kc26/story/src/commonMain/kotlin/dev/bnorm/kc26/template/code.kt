package dev.bnorm.kc26.template

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dev.bnorm.deck.shared.INTELLIJ_LIGHT
import dev.bnorm.deck.shared.JetBrainsMono
import dev.bnorm.storyboard.text.highlight.CodeScope
import dev.bnorm.storyboard.text.highlight.CodeStyle
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.highlight.highlight

val CODE_STYLE = INTELLIJ_LIGHT

fun String.toKotlin(
    scope: CodeScope = CodeScope.File,
    identifierStyle: CodeStyle.(String) -> SpanStyle? = { null },
): AnnotatedString {
    return highlight(CODE_STYLE, Language.Kotlin, scope, identifierStyle = {
        CODE_STYLE.identifierStyle(it)
    })
}

fun AnnotatedString.toKotlin(
    scope: CodeScope = CodeScope.File,
    identifierStyle: CodeStyle.(String) -> SpanStyle? = { _ -> null },
): AnnotatedString {
    val styled = text.toKotlin(
        scope = scope,
        identifierStyle = identifierStyle
    )
    return buildAnnotatedString {
        append(this@toKotlin)
        for (range in styled.spanStyles) {
            addStyle(range.item, range.start, range.end)
        }
    }
}

val Typography.code1: TextStyle
    @Composable
    get() = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 1.sp
    )

val Typography.code2: TextStyle
    @Composable
    get() = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.25.sp
    )

val Typography.code3: TextStyle
    @Composable
    get() = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    )
