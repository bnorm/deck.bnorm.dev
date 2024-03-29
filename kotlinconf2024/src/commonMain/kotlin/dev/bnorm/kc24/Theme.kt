package dev.bnorm.kc24

import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dev.bnorm.librettist.Highlighting
import dev.bnorm.librettist.ShowTheme
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.FontResource

object Theme {
    @OptIn(ExperimentalResourceApi::class)
    private val JetBrainsMono
        @Composable
        get() = FontFamily(
            Font(
                resource = FontResource("font/JetBrainsMono-Thin.ttf"),
                weight = FontWeight.Thin,
                style = FontStyle.Normal
            ),
            Font(
                resource = FontResource("font/JetBrainsMono-ThinItalic.ttf"),
                weight = FontWeight.Thin,
                style = FontStyle.Italic
            ),

            Font(
                resource = FontResource("font/JetBrainsMono-ExtraLight.ttf"),
                weight = FontWeight.ExtraLight,
                style = FontStyle.Normal
            ),
            Font(
                resource = FontResource("font/JetBrainsMono-ExtraLightItalic.ttf"),
                weight = FontWeight.ExtraLight,
                style = FontStyle.Italic
            ),

            Font(
                resource = FontResource("font/JetBrainsMono-Light.ttf"),
                weight = FontWeight.Light,
                style = FontStyle.Normal
            ),
            Font(
                resource = FontResource("font/JetBrainsMono-LightItalic.ttf"),
                weight = FontWeight.Light,
                style = FontStyle.Italic
            ),

            Font(
                resource = FontResource("font/JetBrainsMono-Regular.ttf"),
                weight = FontWeight.Normal,
                style = FontStyle.Normal
            ),
            Font(
                resource = FontResource("font/JetBrainsMono-Italic.ttf"),
                weight = FontWeight.Normal,
                style = FontStyle.Italic
            ),

            Font(
                resource = FontResource("font/JetBrainsMono-Medium.ttf"),
                weight = FontWeight.Medium,
                style = FontStyle.Normal
            ),
            Font(
                resource = FontResource("font/JetBrainsMono-MediumItalic.ttf"),
                weight = FontWeight.Medium,
                style = FontStyle.Italic
            ),

            Font(
                resource = FontResource("font/JetBrainsMono-SemiBold.ttf"),
                weight = FontWeight.SemiBold,
                style = FontStyle.Normal
            ),
            Font(
                resource = FontResource("font/JetBrainsMono-SemiBoldItalic.ttf"),
                weight = FontWeight.SemiBold,
                style = FontStyle.Italic
            ),

            Font(
                resource = FontResource("font/JetBrainsMono-Bold.ttf"),
                weight = FontWeight.Bold,
                style = FontStyle.Normal
            ),
            Font(
                resource = FontResource("font/JetBrainsMono-BoldItalic.ttf"),
                weight = FontWeight.Bold,
                style = FontStyle.Italic
            ),

            Font(
                resource = FontResource("font/JetBrainsMono-ExtraBold.ttf"),
                weight = FontWeight.ExtraBold,
                style = FontStyle.Normal
            ),
            Font(
                resource = FontResource("font/JetBrainsMono-ExtraBoldItalic.ttf"),
                weight = FontWeight.ExtraBold,
                style = FontStyle.Italic
            ),
        )

    val dark: ShowTheme
        @Composable
        get() {
            val jetBrainsMonoTypography = Typography(defaultFontFamily = JetBrainsMono)
            return ShowTheme(
                colors = darkColors(
                    // KotlinConf 2024 website colors
                    // "blue/purple" : 7E53FE or 7F51FF
                    // "red" : FF2757
                    // "orange" : FDB60D

                    background = Color.Black,
                    surface = Color(0xFF1E1F22),
                    onBackground = Color(0xFFBCBEC4),
                    primary = Color(0xFF7F51FF),
                ),
                typography = jetBrainsMonoTypography.copy(
                    h1 = jetBrainsMonoTypography.h1.copy(fontSize = 118.sp, lineHeight = 136.sp),
                    h2 = jetBrainsMonoTypography.h2.copy(fontSize = 98.sp, lineHeight = 118.sp),
                    h3 = jetBrainsMonoTypography.h3.copy(fontSize = 84.sp, lineHeight = 98.sp),
                    h4 = jetBrainsMonoTypography.h3.copy(fontSize = 72.sp, lineHeight = 84.sp),
                    h5 = jetBrainsMonoTypography.h5.copy(fontSize = 62.sp, lineHeight = 72.sp),
                    body1 = jetBrainsMonoTypography.body1.copy(fontSize = 44.sp, lineHeight = 56.sp),
                    body2 = jetBrainsMonoTypography.body2.copy(fontSize = 36.sp, lineHeight = 46.sp),

                    h6 = jetBrainsMonoTypography.h6.copy(fontSize = 0.sp),
                    subtitle1 = jetBrainsMonoTypography.subtitle1.copy(fontSize = 0.sp),
                    subtitle2 = jetBrainsMonoTypography.subtitle2.copy(fontSize = 0.sp),
                    button = jetBrainsMonoTypography.button.copy(fontSize = 0.sp),
                    caption = jetBrainsMonoTypography.caption.copy(fontSize = 0.sp),
                    overline = jetBrainsMonoTypography.overline.copy(fontSize = 0.sp)
                ),
                code = Highlighting(
                    simple = SpanStyle(Color(0xFFBCBEC4)),
                    number = SpanStyle(Color(0xFF2AACB8)),
                    keyword = SpanStyle(Color(0xFFCF8E6D)),
                    punctuation = SpanStyle(Color(0xFFA1C17E)),
                    annotation = SpanStyle(Color(0xFFBBB529)),
                    comment = SpanStyle(Color(0xFF7A7E85)),
                    string = SpanStyle(Color(0xFF6AAB73)),
                    property = SpanStyle(color = Color(0xFFC77DBB)),
                    functionDeclaration = SpanStyle(color = Color(0xFF56A8F5)),
                    extensionFunctionCall = SpanStyle(color = Color(0xFF56A8F5), fontStyle = FontStyle.Italic),
                    staticFunctionCall = SpanStyle(fontStyle = FontStyle.Italic),
                    typeParameters = SpanStyle(color = Color(0xFF16BAAC)),
                )
            )
        }

    // TODO actually make this a proper theme
    val light: ShowTheme
        @Composable
        get() {
            val jetBrainsMonoTypography = Typography(defaultFontFamily = JetBrainsMono)
            return ShowTheme(
                colors = lightColors(
                    background = Color.White,
                    surface = Color(0xFFF5F5F5),
                ),
                typography = jetBrainsMonoTypography,
                code = Highlighting(
                    simple = SpanStyle(Color(0xFF000000)),
                    number = SpanStyle(Color(0xFF4A86E8)),
                    keyword = SpanStyle(Color(0xFF000080)),
                    punctuation = SpanStyle(Color(0xFFA1A1A1)),
                    annotation = SpanStyle(Color(0xFFBBB529)),
                    comment = SpanStyle(Color(0xFF808080)),
                    string = SpanStyle(Color(0xFF067D17)),
                    property = SpanStyle(Color(0xFF000000)),
                    functionDeclaration = SpanStyle(Color(0xFF000000)),
                    extensionFunctionCall = SpanStyle(Color(0xFF000000)),
                    staticFunctionCall = SpanStyle(Color(0xFF000000)),
                    typeParameters = SpanStyle(Color(0xFF000000)),
                )
            )
        }
}
