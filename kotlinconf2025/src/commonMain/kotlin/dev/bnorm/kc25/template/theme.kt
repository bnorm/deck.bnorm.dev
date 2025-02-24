package dev.bnorm.kc25.template

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import dev.bnorm.deck.shared.Inter
import dev.bnorm.deck.shared.JetBrainsMono
import dev.bnorm.storyboard.core.SlideDecorator
import dev.bnorm.storyboard.text.highlight.Highlighting
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

val THEME_DECORATOR = SlideDecorator { content ->
    Highlighting(HIGHLIGHTING) {
        MaterialTheme(
            colors = DARK_COLORS,
            typography = Typography(defaultFontFamily = Inter)
        ) {
            Surface {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Brush.verticalGradient(listOf(Color(0xFF1D002E), Color.Black)))
                ) {
                    content()
                }
            }
        }
    }
}

val DARK_COLORS = darkColors(
    background = Color.Black,
    surface = Color(0xFF1E1F22),
    onBackground = Color(0xFFBCBEC4),
    primary = Color(0xFF7F51FF),
    primaryVariant = Color(0xFF7E53FE),
    secondary = Color(0xFFFDB60D),
)

val HIGHLIGHTING: Highlighting
    @Composable get() {
        val fontFamily = JetBrainsMono
        return Highlighting.Companion.build {
            simple += SpanStyle(color = Color(0xFFBCBEC4), fontFamily = fontFamily)
            number = simple + SpanStyle(color = Color(0xFF2AACB8))
            keyword = simple + SpanStyle(color = Color(0xFFCF8E6D))
            punctuation = simple + SpanStyle(color = Color(0xFFA1C17E))
            annotation = simple + SpanStyle(color = Color(0xFFBBB529))
            comment = simple + SpanStyle(color = Color(0xFF7A7E85))
            string = simple + SpanStyle(color = Color(0xFF6AAB73))
            property = simple + SpanStyle(color = Color(0xFFC77DBB))
            staticProperty = property + SpanStyle(fontStyle = FontStyle.Italic)
            functionDeclaration = simple + SpanStyle(color = Color(0xFF56A8F5))
            extensionFunctionCall = simple + SpanStyle(
                color = Color(0xFF56A8F5),
                fontStyle = FontStyle.Companion.Italic
            )
            staticFunctionCall = simple + SpanStyle(fontStyle = FontStyle.Companion.Italic)
            typeParameters = simple + SpanStyle(color = Color(0xFF16BAAC))
        }
    }

fun <T> defaultSpec(
    duration: Duration = 300.milliseconds,
    delay: Duration = Duration.ZERO,
    easing: Easing = FastOutSlowInEasing,
): FiniteAnimationSpec<T> {
    return tween(
        durationMillis = duration.inWholeMilliseconds.toInt(),
        delayMillis = delay.inWholeMilliseconds.toInt(),
        easing = easing,
    )
}
