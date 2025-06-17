package dev.bnorm.deck.shared

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import dev.bnorm.storyboard.text.highlight.CodeStyle

val INTELLIJ_LIGHT = CodeStyle.build {
    simple += SpanStyle(color = Color(0xFF080808))
    number = simple + SpanStyle(color = Color(0xFF1750EB))
    keyword = simple + SpanStyle(color = Color(0xFFC0033B3))
    punctuation = simple
    annotation = simple + SpanStyle(color = Color(0xFF9E880D))
    comment = simple + SpanStyle(color = Color(0xFF8C8C8C))
    string = simple + SpanStyle(color = Color(0xFF067D17))
    property = simple + SpanStyle(color = Color(0xFF871094))
    staticProperty = property + SpanStyle(fontStyle = FontStyle.Italic)
    functionDeclaration = simple + SpanStyle(color = Color(0xFF00627A))
    extensionFunctionCall = simple + SpanStyle(color = Color(0xFF00627A), fontStyle = FontStyle.Italic)
    staticFunctionCall = functionDeclaration + SpanStyle(fontStyle = FontStyle.Italic)
    typeParameters = simple + SpanStyle(color = Color(0xFF007E8A))
    namedArgument = simple + SpanStyle(color = Color(0xFF4A86E8))
    label = simple + SpanStyle(color = Color(0xFF4A86E8))
}

val INTELLIJ_DARK = CodeStyle.build {
    simple += SpanStyle(color = Color(0xFFBCBEC4))
    number = simple + SpanStyle(color = Color(0xFF2AACB8))
    keyword = simple + SpanStyle(color = Color(0xFFCF8E6D))
//    punctuation = simple + SpanStyle(color = Color(0xFFA1C17E))
    annotation = simple + SpanStyle(color = Color(0xFFBBB529))
    comment = simple + SpanStyle(color = Color(0xFF7A7E85))
    string = simple + SpanStyle(color = Color(0xFF6AAB73))
    property = simple + SpanStyle(color = Color(0xFFC77DBB))
    staticProperty = property + SpanStyle(fontStyle = FontStyle.Italic)
    functionDeclaration = simple + SpanStyle(color = Color(0xFF56A8F5))
    extensionFunctionCall = simple + SpanStyle(color = Color(0xFF56A8F5), fontStyle = FontStyle.Italic)
    staticFunctionCall = simple + SpanStyle(fontStyle = FontStyle.Italic)
    typeParameters = simple + SpanStyle(color = Color(0xFF16BAAC))
    namedArgument = simple + SpanStyle(color = Color(0xFF56C1D6))
    label = simple + SpanStyle(color = Color(0xFF32B8AF))
}
