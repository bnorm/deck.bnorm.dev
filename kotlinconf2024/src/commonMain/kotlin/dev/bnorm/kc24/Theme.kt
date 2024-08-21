package dev.bnorm.kc24

import androidx.compose.material.Colors
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp
import dev.bnorm.deck.shared.Inter
import dev.bnorm.deck.shared.JetBrainsMono
import dev.bnorm.storyboard.text.highlight.Highlighting

object Theme {
    val codeStyle: Highlighting
        @Composable
        get() {
            /*
                Highlighting.build {
                    simple = SpanStyle(Color(0xFF000000))
                    number = SpanStyle(Color(0xFF4A86E8))
                    keyword = SpanStyle(Color(0xFF000080))
                    punctuation = SpanStyle(Color(0xFFA1A1A1))
                    annotation = SpanStyle(Color(0xFFBBB529))
                    comment = SpanStyle(Color(0xFF808080))
                    string = SpanStyle(Color(0xFF067D17))
                    property = SpanStyle(Color(0xFF000000))
                    functionDeclaration = SpanStyle(Color(0xFF000000))
                    extensionFunctionCall = SpanStyle(Color(0xFF000000))
                    staticFunctionCall = SpanStyle(Color(0xFF000000))
                    typeParameters = SpanStyle(Color(0xFF000000))
                }
             */

            val family = JetBrainsMono
            return Highlighting.build {
                simple = SpanStyle(Color(0xFFBCBEC4), fontFamily = family)
                number = simple + SpanStyle(Color(0xFF2AACB8))
                keyword = simple + SpanStyle(Color(0xFFCF8E6D))
                punctuation = simple + SpanStyle(Color(0xFFA1C17E))
                annotation = simple + SpanStyle(Color(0xFFBBB529))
                comment = simple + SpanStyle(Color(0xFF7A7E85))
                string = simple + SpanStyle(Color(0xFF6AAB73))
                property = simple + SpanStyle(color = Color(0xFFC77DBB))
                functionDeclaration = simple + SpanStyle(color = Color(0xFF56A8F5))
                extensionFunctionCall = functionDeclaration + SpanStyle(fontStyle = FontStyle.Italic)
                staticFunctionCall = simple + SpanStyle(fontStyle = FontStyle.Italic)
                typeParameters = simple + SpanStyle(color = Color(0xFF16BAAC))
            }
        }

    val dark: Colors
        @Composable
        get() = darkColors(
            // KotlinConf 2024 website colors
            // "blue/purple" : 7E53FE or 7F51FF
            // "red" : FF2757
            // "orange" : FDB60D

            background = Color.Black,
            surface = Color(0xFF1E1F22),
            onBackground = Color(0xFFBCBEC4),
            primary = Color(0xFF7F51FF),
        )

    val typography: Typography
        @Composable
        get() {
            val jetBrainsMonoTypography = Typography(defaultFontFamily = Inter)
            return jetBrainsMonoTypography.copy(
                h1 = jetBrainsMonoTypography.h1.copy(fontSize = 118.sp, lineHeight = 124.sp),
                h2 = jetBrainsMonoTypography.h2.copy(fontSize = 98.sp, lineHeight = 118.sp),
                h3 = jetBrainsMonoTypography.h3.copy(fontSize = 84.sp, lineHeight = 98.sp),
                h4 = jetBrainsMonoTypography.h3.copy(fontSize = 72.sp, lineHeight = 84.sp),
                h5 = jetBrainsMonoTypography.h5.copy(fontSize = 62.sp, lineHeight = 72.sp),
                h6 = jetBrainsMonoTypography.h5.copy(fontSize = 56.sp, lineHeight = 62.sp),
                subtitle1 = jetBrainsMonoTypography.subtitle1.copy(fontSize = 28.sp, lineHeight = 36.sp),
                body1 = jetBrainsMonoTypography.body1.copy(fontSize = 44.sp, lineHeight = 56.sp),
                body2 = jetBrainsMonoTypography.body2.copy(fontSize = 36.sp, lineHeight = 46.sp),

                subtitle2 = jetBrainsMonoTypography.subtitle2.copy(fontSize = 0.sp),
                button = jetBrainsMonoTypography.button.copy(fontSize = 0.sp),
                caption = jetBrainsMonoTypography.caption.copy(fontSize = 0.sp),
                overline = jetBrainsMonoTypography.overline.copy(fontSize = 0.sp)
            )
        }

    // TODO actually make this a proper theme
    val light: Colors
        @Composable
        get() = lightColors(
            background = Color.White,
            surface = Color(0xFFF5F5F5),
        )
}
