package dev.bnorm.kc26.template

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dev.bnorm.deck.shared.Inter
import dev.bnorm.storyboard.ContentDecorator

fun themeDecorator(): ContentDecorator = ContentDecorator { content ->
    MaterialTheme(
        colors = SCENE_COLORS,
        typography = Kc26Typography(),
    ) {
        content()
    }
}

val SCENE_COLORS = lightColors(
    background = Color.White,
    onBackground = Color.Black,

    surface = Color.White.copy(alpha = 0.9f),
    onSurface = Color.Black,

    primary = Kc26Colors.magenta100,
    primaryVariant = Kc26Colors.purple100,
    secondary = Kc26Colors.orange,
    secondaryVariant = Kc26Colors.orangeTextDark,
)

val Typography.title: TextStyle
    @Composable
    get() = h2.copy(
        fontWeight = FontWeight.SemiBold,
    )

val Typography.header: TextStyle
    @Composable
    get() = h3.copy(
        fontWeight = FontWeight.SemiBold,
    )

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

// JetBrains' Brand Colors. Stolen from KotlinConf App.
// https://github.com/JetBrains/kotlinconf-app/blob/a69a1462b4c98793fd9ce40836ac86f3099928d7/app/ui-components/src/commonMain/kotlin/org/jetbrains/kotlinconf/ui/theme/ColorValues.kt
object Kc26Colors {
    val purple100 = Color(0xFF8F00E7)
    val purple90 = Color(0xE58F00E7)
    val purple80 = Color(0xCC8F00E7)
    val purple70 = Color(0xB28F00E7)
    val purple60 = Color(0x998F00E7)
    val purple50 = Color(0x808F00E7)
    val purple40 = Color(0x668F00E7)
    val purple30 = Color(0x4D8F00E7)
    val purple20 = Color(0x338F00E7)
    val purple10 = Color(0x1A8F00E7)
    val purple05 = Color(0x0D8F00E7)

    val magenta100 = Color(0xFFC202D7)
    val magenta90 = Color(0xE5C202D7)
    val magenta80 = Color(0xCCC202D7)
    val magenta70 = Color(0xB2C202D7)
    val magenta60 = Color(0x99C202D7)
    val magenta50 = Color(0x80C202D7)
    val magenta40 = Color(0x66C202D7)
    val magenta30 = Color(0x4DC202D7)
    val magenta20 = Color(0x33C202D7)
    val magenta10 = Color(0x1AC202D7)
    val magenta05 = Color(0x0DC202D7)

    val pink100 = Color(0xFFE00189)
    val pink90 = Color(0xE5E00189)
    val pink80 = Color(0xCCE00189)
    val pink70 = Color(0xB2E00189)
    val pink60 = Color(0x99E00189)
    val pink50 = Color(0x80E00189)
    val pink40 = Color(0x66E00189)
    val pink30 = Color(0x4DE00189)
    val pink20 = Color(0x33E00189)
    val pink10 = Color(0x1AE00189)
    val pink05 = Color(0x0DE00189)

    val orange = Color(0xFFFF5A13)

    val purpleTextDark = Color(0xFFBF56FF)
    val magentaTextDark = Color(0xFFED44FF)
    val pinkTextDark = Color(0xFFFF66C3)
    val orangeTextDark = Color(0xFFFF9100)

    // TODO is the gradient too intense?
    private fun Color.darken(): Color = lerp(Color.Gray, this, 0.9f)
    val gradientColors = listOf(purple100.darken(), pink100.darken(), orangeTextDark.darken())
    val colorGradient = Brush.linearGradient(
        colors = gradientColors,
        start = Offset(0f, Float.POSITIVE_INFINITY),
        end = Offset(Float.POSITIVE_INFINITY, 0f),
    )
}
