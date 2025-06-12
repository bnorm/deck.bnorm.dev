package dev.bnorm.dcnyc25.template

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Typography
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dev.bnorm.dcnyc25.broadcast.LocalVoteTally
import dev.bnorm.dcnyc25.broadcast.VoteTally
import dev.bnorm.deck.shared.Inter
import dev.bnorm.deck.shared.JetBrainsMono
import dev.bnorm.storyboard.SceneDecorator

fun storyDecorator(tally: VoteTally? = null): SceneDecorator = SceneDecorator { content ->
    MaterialTheme(
        colors = COLORS,
        typography = dcnyc25Typography(),
    ) {
        CompositionLocalProvider(LocalVoteTally provides tally) {
            Surface {
                content()
            }
        }
    }
}

val COLORS = lightColors(
    primary = Color(0xFFFF7156),
    onPrimary = Color(0xFFF8F8F8),
    secondary = Color(0xFF050CEB),
    onSecondary = Color(0xFFF8F8F8),
)

val Typography.code1: TextStyle
    @Composable
    get() = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
    )

val Typography.code2: TextStyle
    @Composable
    get() = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    )

val Typography.code3: TextStyle
    @Composable
    get() = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
    )


@Composable
private fun dcnyc25Typography(): Typography {
    val typography = Typography(defaultFontFamily = Inter)
    return typography.copy(
        h1 = typography.h1.copy(fontWeight = FontWeight.ExtraLight),
        h2 = typography.h2.copy(fontWeight = FontWeight.ExtraLight),
        h3 = typography.h3.copy(fontWeight = FontWeight.ExtraLight),
        h4 = typography.h4.copy(fontWeight = FontWeight.ExtraLight),
        h5 = typography.h5.copy(fontWeight = FontWeight.ExtraLight),
        h6 = typography.h6.copy(fontWeight = FontWeight.ExtraLight),
        body1 = typography.body1.copy(
            fontWeight = FontWeight.ExtraLight,
            fontSize = 24.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.sp
        ),
    )
}
