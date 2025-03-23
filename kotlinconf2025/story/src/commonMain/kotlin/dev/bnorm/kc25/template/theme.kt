package dev.bnorm.kc25.template

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
import dev.bnorm.storyboard.core.SceneDecorator
import dev.bnorm.storyboard.text.highlight.Highlighting

val THEME_DECORATOR = SceneDecorator { content ->
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

// Conference Palate
// Yellow: 0xFFFF941A
// Light purple: 0xFFC201D7
// Dark purple: 0xFF480079

val CONFERENCE_YELLOW = Color(0xFFFF9419)
val CONFERENCE_RED = Color(0xFFFF021D)
val CONFERENCE_PURPLE = Color(0xFFE600FF)

val DARK_COLORS = darkColors(
    background = Color.Black,
    surface = Color(0xFF1D002E),
    onBackground = Color(0xFFBCBEC4),
    primary = Color(0xFFC201D7),
    primaryVariant = Color(0xFF480079),
    secondary = Color(0xFFFF941A),
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
