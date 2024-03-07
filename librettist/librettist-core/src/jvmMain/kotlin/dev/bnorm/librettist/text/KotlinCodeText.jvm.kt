package dev.bnorm.librettist.text

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import dev.bnorm.librettist.LocalShowTheme
import org.intellij.lang.annotations.Language
import org.jetbrains.kotlin.lexer.KotlinLexer
import org.jetbrains.kotlin.lexer.KtKeywordToken
import org.jetbrains.kotlin.lexer.KtToken
import org.jetbrains.kotlin.lexer.KtTokens

// TODO we need some kind of Kotlin/Multiplatform version of hightlight.js if we want to move this to common...

@Composable
actual fun KotlinCodeString(
    @Language("Kotlin") text: String,
    identifierType: (String) -> SpanStyle?,
): AnnotatedString = buildAnnotatedString {
    val codeTheme = LocalShowTheme.current.code
    withStyle(codeTheme.simple) {
        append(text)

        KotlinLexer().apply {
            start(text)

            while (true) {
                when (tokenType as? KtToken) {
                    KtTokens.EOF -> break // Exit
                    null -> if (tokenStart == length) break // Exit
                    KtTokens.WHITE_SPACE -> Unit // Do nothing

                    KtTokens.LBRACE,
                    KtTokens.RBRACE,
                    KtTokens.LPAR,
                    KtTokens.RPAR,
                    KtTokens.LT,
                    KtTokens.GT,
                    KtTokens.EQ,
                    KtTokens.EQEQ,
                    KtTokens.DOT,
                    KtTokens.COMMA,
                    KtTokens.COLON,
                    KtTokens.PLUS,
                    KtTokens.MUL,
                    -> Unit

                    KtTokens.IDENTIFIER -> {
                        val style = identifierType(tokenText)
                        if (style != null) addStyle(style, tokenStart, tokenEnd)
                    }

                    KtTokens.INTEGER_LITERAL,
                    KtTokens.FLOAT_LITERAL,
                    -> addStyle(codeTheme.number, tokenStart, tokenEnd)

                    KtTokens.OPEN_QUOTE -> {
                        while (tokenType != KtTokens.CLOSING_QUOTE) {
                            when (tokenType) {
                                KtTokens.EOF -> break // Exit
                                null -> if (tokenStart == length) break // Exit

                                KtTokens.SHORT_TEMPLATE_ENTRY_START -> {
                                    addStyle(codeTheme.keyword, tokenStart, tokenEnd)
                                    advance()
                                    val type = tokenType
                                    if (type != KtTokens.IDENTIFIER) {
                                        addStyle(codeTheme.string, tokenStart, tokenEnd)
                                        continue
                                    }
                                }

                                else -> addStyle(codeTheme.string, tokenStart, tokenEnd)
                            }
                            advance()
                        }
                        addStyle(codeTheme.string, tokenStart, tokenEnd)
                    }

                    KtTokens.CHARACTER_LITERAL -> {
                        addStyle(codeTheme.string, tokenStart, tokenEnd)
                        advance()

                        while (tokenType != KtTokens.CHARACTER_LITERAL) {
                            when (tokenType) {
                                KtTokens.EOF -> break // Exit
                                null -> if (tokenStart == length) break // Exit
                                else -> addStyle(codeTheme.string, tokenStart, tokenEnd)
                            }
                            advance()
                        }
                        addStyle(codeTheme.string, tokenStart, tokenEnd)
                    }

                    KtTokens.AT -> {
                        val start = tokenStart
                        advance()
                        if (tokenType != KtTokens.IDENTIFIER) {
                            addStyle(codeTheme.annotation, start, tokenStart)
                            continue  // Skip advancement
                        } else {
                            addStyle(codeTheme.annotation, start, tokenEnd)
                        }
                    }

                    in KtTokens.COMMENTS -> addStyle(codeTheme.comment, tokenStart, tokenEnd)

                    is KtKeywordToken,
                    -> addStyle(codeTheme.keyword, tokenStart, tokenEnd)

                    else -> error("unknown token type: $tokenType")
                }

                advance()
            }
        }
    }
}