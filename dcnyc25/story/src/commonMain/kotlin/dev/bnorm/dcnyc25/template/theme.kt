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
import androidx.compose.ui.unit.dp
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

val YesColor = Color.LightGray
val NoColor = Color.DarkGray

val MatchColor = Color.Blue.copy(alpha = 0.5f)
val AddColor = Color.Green.copy(alpha = 0.5f)
val DeleteColor = Color.Red.copy(alpha = 0.5f)

val Typography.code1: TextStyle
    @Composable
    get() = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 24.sp,
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
