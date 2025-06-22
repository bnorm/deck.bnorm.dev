package dev.bnorm.dcnyc25.template

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Typography
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bnorm.deck.shared.Inter
import dev.bnorm.deck.shared.JetBrainsMono
import dev.bnorm.storyboard.SceneDecorator

fun storyDecorator(): SceneDecorator = SceneDecorator { content ->
    MaterialTheme(
        colors = COLORS,
        typography = dcnyc25Typography(),
    ) {
        Surface {
            content()
        }
    }
}

val COLORS = lightColors(
    primary = Color(0xFFFF7156), // TODO alternative 0xFFF05E42
    onPrimary = Color(0xFFF8F8F8),
    secondary = Color(0xFF050CEB), // TODO alternative 0xFF171EEB
    onSecondary = Color(0xFFF8F8F8),
)

val MatchColor = Color.Blue.copy(alpha = 0.4f)
val AddColor = Color.Green.copy(alpha = 0.5f)
val DeleteColor = Color.Red.copy(alpha = 0.4f)

val Typography.code1: TextStyle
    @Composable
    get() = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 24.sp,
    )

val Typography.code2: TextStyle
    @Composable
    get() = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 18.sp,
    )

val EmphasisWeight = FontWeight.ExtraBold
val BulletSpacing = 16.dp

@Composable
private fun dcnyc25Typography(): Typography {
    val typography = Typography(defaultFontFamily = Inter)
    return typography.copy(
        h1 = typography.h1.copy(fontWeight = FontWeight.Light),
        h2 = typography.h2.copy(fontWeight = FontWeight.Light),
        h3 = typography.h3.copy(fontWeight = FontWeight.Light),
        h4 = typography.h4.copy(fontWeight = FontWeight.Light),
        h5 = typography.h5.copy(fontWeight = FontWeight.Light),
        h6 = typography.h6.copy(fontWeight = FontWeight.Light),
        body1 = typography.body1.copy(
            fontWeight = FontWeight.Light,
            fontSize = 22.sp,
            lineHeight = 26.sp,
            letterSpacing = 0.sp
        ),
    )
}
