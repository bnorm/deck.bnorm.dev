package dev.bnorm.librettist.text

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import dev.bnorm.librettist.LocalSlideTheme
import org.intellij.lang.annotations.Language
import org.jetbrains.kotlin.lexer.KotlinLexer
import org.jetbrains.kotlin.lexer.KtKeywordToken
import org.jetbrains.kotlin.lexer.KtToken
import org.jetbrains.kotlin.lexer.KtTokens

// TODO we need some kind of Kotlin/Multiplatform version of hightlight.js if we want to move this to common...

@Composable
fun KotlinCodeText(
    @Language("Kotlin") text: String,
    modifier: Modifier = Modifier,
    identifierType: (String) -> SpanStyle? = { null },
) {
    Column(modifier) {
        for (line in text.lines()) {
            Text(text = KotlinCodeString(line, identifierType))
        }
    }
}

@Composable
fun KotlinCodeString(
    @Language("Kotlin") text: String,
    identifierType: (String) -> SpanStyle? = { null },
): AnnotatedString = buildAnnotatedString {
    withStyle(LocalSlideTheme.current.code.simple) {
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
                    -> addStyle(LocalSlideTheme.current.code.number, tokenStart, tokenEnd)

                    KtTokens.OPEN_QUOTE -> {
                        while (tokenType != KtTokens.CLOSING_QUOTE) {
                            when (tokenType) {
                                KtTokens.EOF -> break // Exit
                                null -> if (tokenStart == length) break // Exit

                                KtTokens.SHORT_TEMPLATE_ENTRY_START -> {
                                    addStyle(LocalSlideTheme.current.code.keyword, tokenStart, tokenEnd)
                                    advance()
                                    val type = tokenType
                                    if (type != KtTokens.IDENTIFIER) {
                                        addStyle(LocalSlideTheme.current.code.string, tokenStart, tokenEnd)
                                        continue
                                    }
                                }

                                else -> addStyle(LocalSlideTheme.current.code.string, tokenStart, tokenEnd)
                            }
                            advance()
                        }
                        addStyle(LocalSlideTheme.current.code.string, tokenStart, tokenEnd)
                    }

                    KtTokens.CHARACTER_LITERAL -> {
                        addStyle(LocalSlideTheme.current.code.string, tokenStart, tokenEnd)
                        advance()

                        while (tokenType != KtTokens.CHARACTER_LITERAL) {
                            when (tokenType) {
                                KtTokens.EOF -> break // Exit
                                null -> if (tokenStart == length) break // Exit
                                else -> addStyle(LocalSlideTheme.current.code.string, tokenStart, tokenEnd)
                            }
                            advance()
                        }
                        addStyle(LocalSlideTheme.current.code.string, tokenStart, tokenEnd)
                    }

                    KtTokens.AT -> {
                        val start = tokenStart
                        advance()
                        if (tokenType != KtTokens.IDENTIFIER) {
                            addStyle(LocalSlideTheme.current.code.annotation, start, tokenStart)
                            continue  // Skip advancement
                        } else {
                            addStyle(LocalSlideTheme.current.code.annotation, start, tokenEnd)
                        }
                    }

                    in KtTokens.COMMENTS -> addStyle(LocalSlideTheme.current.code.comment, tokenStart, tokenEnd)

                    is KtKeywordToken,
                    -> addStyle(LocalSlideTheme.current.code.keyword, tokenStart, tokenEnd)

                    else -> error("unknown token type: $tokenType")
                }

                advance()
            }
        }
    }
}