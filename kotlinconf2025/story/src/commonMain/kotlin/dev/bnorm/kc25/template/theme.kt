package dev.bnorm.kc25.template

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dev.bnorm.deck.shared.Inter
import dev.bnorm.deck.shared.JetBrainsMono
import dev.bnorm.storyboard.core.SceneDecorator
import dev.bnorm.storyboard.text.highlight.CacheableHighlighter
import dev.bnorm.storyboard.text.highlight.Highlighting
import dev.bnorm.storyboard.text.highlight.KotlinHighlighter
import dev.bnorm.storyboard.text.highlight.LocalHighlighter

val THEME_DECORATOR = SceneDecorator { content ->
    val highlighting = HIGHLIGHTING
    var highlighter = remember(highlighting) { CacheableHighlighter(KotlinHighlighter(highlighting)) }
    CompositionLocalProvider(LocalHighlighter provides highlighter) {
        MaterialTheme(
            colors = DARK_COLORS,
            typography = Kc25Typography(),
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

val BIRD_YELLOW = Color(0xFFFF9419)
val BIRD_RED = Color(0xFFFF021D)
val BIRD_PURPLE = Color(0xFFE600FF)

val DARK_COLORS = darkColors(
    background = Color.Black,
    surface = Color(0xFF1D002E),
    onBackground = Color(0xFFBCBEC4),
    primary = Color(0xFF8854FF),
    secondary = Color(0xFFFF5800),
)

val HIGHLIGHTING: Highlighting
    @Composable get() {
        val fontFamily = JetBrainsMono
        return Highlighting.build {
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
            extensionFunctionCall = simple + SpanStyle(color = Color(0xFF56A8F5), fontStyle = FontStyle.Italic)
            staticFunctionCall = simple + SpanStyle(fontStyle = FontStyle.Italic)
            typeParameters = simple + SpanStyle(color = Color(0xFF16BAAC))
        }
    }

val Typography.code1: TextStyle
    @Composable
    get() = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.5.sp
    )

val Typography.code2: TextStyle
    @Composable
    get() = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    )


@Composable
private fun Kc25Typography(): Typography {
    val typography = Typography(defaultFontFamily = Inter)
    return typography.copy(
        body1 = typography.body1.copy(
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.sp
        )
    )
}
