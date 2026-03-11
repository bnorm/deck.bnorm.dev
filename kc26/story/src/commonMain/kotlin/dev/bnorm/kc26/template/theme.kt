package dev.bnorm.kc26.template

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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dev.bnorm.deck.shared.INTELLIJ_DARK
import dev.bnorm.deck.shared.Inter
import dev.bnorm.deck.shared.JetBrainsMono
import dev.bnorm.storyboard.ContentDecorator
import dev.bnorm.storyboard.text.highlight.CodeScope
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.highlight.highlight

fun themeDecorator(): ContentDecorator = ContentDecorator { content ->
    MaterialTheme(
        colors = SCENE_COLORS,
        typography = Kc26Typography(),
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

// TODO i think i want to do a light theme this time...
val SCENE_COLORS = darkColors(
    background = Color.Black,
    surface = Color(0xFF1D002E),
    onBackground = Color(0xFFBCBEC4),
    primary = Color(0xFF8854FF),
    secondary = Color(0xFFFF5800),
)

val Typography.title: TextStyle
    @Composable
    get() = h2.copy(
        fontWeight = FontWeight.Normal,
    )

val Typography.header: TextStyle
    @Composable
    get() = h3

@Composable
private fun Kc26Typography(): Typography {
    val typography = Typography(defaultFontFamily = Inter)
    return typography.copy(
        body1 = typography.body1.copy(
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp
        ),
        body2 = typography.body2.copy(
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.sp
        ),
        caption = typography.h5,
    )
}
