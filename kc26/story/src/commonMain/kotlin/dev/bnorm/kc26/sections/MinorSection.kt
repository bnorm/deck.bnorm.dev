package dev.bnorm.kc26.sections

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import dev.bnorm.deck.shared.code.CodeSample
import dev.bnorm.deck.shared.code.buildCodeSamples
import dev.bnorm.kc26.components.GradientText
import dev.bnorm.kc26.components.OutputComparison
import dev.bnorm.kc26.components.SceneCodeSample
import dev.bnorm.kc26.components.VersionCompareState
import dev.bnorm.kc26.template.CODE_STYLE
import dev.bnorm.kc26.template.carouselScene
import dev.bnorm.kc26.template.toKotlin
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.mapToValue
import dev.bnorm.storyboard.text.highlight.CodeScope
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.toValue

fun StoryboardBuilder.MinorSection() {
    // TICKETS:
    //
    // Kotlin 2.1
    // https://youtrack.jetbrains.com/issue/KT-69646/PowerAssert-result-of-array-access-operator-is-unaligned
    // https://youtrack.jetbrains.com/issue/KT-69038/Power-Assert-does-not-display-const-vals
    // https://youtrack.jetbrains.com/issue/KT-73870/PowerAssert-Object-should-not-be-displayed
    // https://youtrack.jetbrains.com/issue/KT-73871/PowerAssert-Comparison-via-operator-overload-results-in-confusing-diagram
    //
    // Kotlin 2.2
    // https://youtrack.jetbrains.com/issue/KT-75663/PowerAssert-contains-result-for-strings-is-displayed-under-the-first-parameter-instead-of-in
    // https://youtrack.jetbrains.com/issue/KT-75264/PowerAssert-the-diagram-for-try-catch-with-boolean-expressions-isnt-clear
    // https://youtrack.jetbrains.com/issue/KT-75263/PowerAssert-no-additional-info-is-displayed-for-when-with-subject
    // https://youtrack.jetbrains.com/issue/KT-75876/PowerAssert-dont-display-results-for-assertion-operator
    // https://youtrack.jetbrains.com/issue/KT-75265/PowerAssert-the-result-of-invoke-is-displayed-at-the-same-level-as-value-that-can-be-confusing
    //
    // Kotlin 2.4
    // https://youtrack.jetbrains.com/issue/KT-75873/PowerAssert-display-callable-reference-value-under

    Kotlin21Improvements()
    Kotlin22Improvements()
    Kotlin23Problems()
    Kotlin23Implementation()
    // TODO scene showing existing problems
}

private fun StoryboardBuilder.Kotlin21Improvements() {
    carouselScene(
        frames = listOf(
            VersionCompareState.Hidden,
            VersionCompareState.Before,
            VersionCompareState.After,
        ),
    ) {
        Column {
            SceneCodeSample(
                content = {
                    Text(
                        """
                            @Test fun test() {
                                assert(listOf("Hello", "World")[1] == "Hello")
                            }
                        """.trimIndent().toKotlin {
                            when (it) {
                                "assert" -> staticFunctionCall
                                else -> null
                            }
                        }
                    )
                },
                output = {
                    OutputComparison(
                        beforeVersion = { GradientText("2.0") },
                        beforeOutput = {
                            Text(
                                """
                                    assert(listOf("Hello", "World")[1] == "Hello")
                                           |                           |
                                           |                           false
                                           World
                                           [Hello, World]
                                """.trimIndent()
                            )
                        },
                        afterVersion = { GradientText("2.1") },
                        afterOutput = {
                            Text(
                                """
                                    assert(listOf("Hello", "World")[1] == "Hello")
                                           |                       |   |
                                           |                       |   false
                                           |                       World
                                           [Hello, World]
                                """.trimIndent()
                            )
                        },
                        showAfter = transition.createChildTransition { frame ->
                            frame.mapToValue(start = false, end = true) { it == VersionCompareState.After }
                        },
                    )
                },
                hideOutput = transition.createChildTransition { frame ->
                    frame.mapToValue(start = true, end = true) { it == VersionCompareState.Hidden }
                },
            )
        }
    }
}

private fun StoryboardBuilder.Kotlin22Improvements() {
    carouselScene(
        frames = listOf(
            VersionCompareState.Hidden,
            VersionCompareState.Before,
            VersionCompareState.After,
        ),
    ) {
        Column {
            SceneCodeSample(
                content = {
                    Text(
                        """
                            @Test fun test() {
                                val a : Boolean? = false
                                assert(a!!)
                            }
                        """.trimIndent().toKotlin {
                            when (it) {
                                "assert", "listOf" -> staticFunctionCall
                                else -> null
                            }
                        }
                    )
                },
                output = {
                    OutputComparison(
                        beforeVersion = { GradientText("2.1") },
                        beforeOutput = {
                            Text(
                                """
                                    assert(a!!)
                                           ||
                                           |false
                                           false
                                """.trimIndent()
                            )
                        },
                        afterVersion = { GradientText("2.2") },
                        afterOutput = {
                            Text(
                                """
                                    assert(a!!)
                                           |
                                           |
                                           false
                                """.trimIndent()
                            )
                        },
                        showAfter = transition.createChildTransition { frame ->
                            frame.mapToValue(start = false, end = true) { it == VersionCompareState.After }
                        },
                    )
                },
                hideOutput = transition.createChildTransition { frame ->
                    frame.mapToValue(start = true, end = true) { it == VersionCompareState.Hidden }
                },
            )
        }
    }
}

private fun StoryboardBuilder.Kotlin23Problems() {
    val sample = """
        @Test fun test() {
            assert(arrayOf(true, false)[1])
        }
    """.trimIndent().toKotlin {
        when (it) {
            "assert", "listOf" -> staticFunctionCall
            else -> null
        }
    }

    val output = """
        assert(arrayOf(true, false)[1])
               |                   |
               |                   false
               [Ljava.lang.Boolean;@47caedad
    """.trimIndent()

    carouselScene(
        frames = listOf(
            VersionCompareState.Hidden,
            VersionCompareState.Before,
        ),
    ) {
        Column {
            SceneCodeSample(
                content = { Text(sample) },
                output = {
                    OutputComparison(
                        version = { GradientText("2.3") },
                        output = { Text(output) },
                    )
                },
                hideOutput = transition.createChildTransition { frame ->
                    frame.mapToValue(start = true, end = true) { it == VersionCompareState.Hidden }
                },
            )
        }
    }
}

private fun StoryboardBuilder.Kotlin23Implementation() {
    val samples = buildCodeSamples {
        fun String.toCodeSample(): CodeSample {
            return toCodeSample(CODE_STYLE, scope = CodeScope.Function) { identifier ->
                when (identifier) {
                    "assert" -> CODE_STYLE.staticFunctionCall
                    "trimIndent" -> CODE_STYLE.extensionFunctionCall
                    "length" -> CODE_STYLE.property
                    else -> null
                }
            }
        }

        val samples = listOf(
            """
                assert(str.length >= 1 && str[0] == 'x')
            """.trimIndent(),
            """
                val tmp1 = str
                assert(tmp1.length >= 1 && str[0] == 'x')
            """.trimIndent(),
            """
                val tmp1 = str
                val tmp2 = tmp1.length
                assert(tmp2 >= 1 && str[0] == 'x')
            """.trimIndent(),
            """
                val tmp1 = str
                val tmp2 = tmp1.length
                val tmp3 = tmp2 >= 1
                assert(tmp3 && str[0] == 'x')
            """.trimIndent(),
            """
                val tmp1 = str
                val tmp2 = tmp1.length
                val tmp3 = tmp2 >= 1
                assert(when {
                    tmp3 -> str[0] == 'x'
                    else -> false
                })
            """.trimIndent(),
            """
                val tmp1 = str
                val tmp2 = tmp1.length
                val tmp3 = tmp2 >= 1
                when {
                    tmp3 -> assert(str[0] == 'x')
                    else -> assert(false)
                }
            """.trimIndent(),
            """
                val tmp1 = str
                val tmp2 = tmp1.length
                val tmp3 = tmp2 >= 1
                when {
                    tmp3 -> {
                        val tmp4 = str
                        assert(tmp4[0] == 'x')
                    }
                    else -> assert(false)
                }
            """.trimIndent(),
            """
                val tmp1 = str
                val tmp2 = tmp1.length
                val tmp3 = tmp2 >= 1
                when {
                    tmp3 -> {
                        val tmp4 = str
                        val tmp5 = tmp4[0]
                        assert(tmp5 == 'x')
                    }
                    else -> assert(false)
                }
            """.trimIndent(),
            """
                val tmp1 = str
                val tmp2 = tmp1.length
                val tmp3 = tmp2 >= 1
                when {
                    tmp3 -> {
                        val tmp4 = str
                        val tmp5 = tmp4[0]
                        val tmp6 = tmp5 == 'x'
                        assert(tmp6)
                    }
                    else -> assert(false)
                }
            """.trimIndent(),
            """
                val tmp1 = str
                val tmp2 = tmp1.length
                val tmp3 = tmp2 >= 1
                when {
                    tmp3 -> {
                        val tmp4 = str
                        val tmp5 = tmp4[0]
                        val tmp6 = tmp5 == 'x'
                        assert(tmp6)
                    }
                    else -> assert(false)
                }
            """.trimIndent(),
            """
                val tmp1 = str
                val tmp2 = tmp1.length
                val tmp3 = tmp2 >= 1
                when {
                    tmp3 -> {
                        val tmp4 = str
                        val tmp5 = tmp4[0]
                        val tmp6 = tmp5 == 'x'
                        assert(tmp6)
                    }
                    else -> assert(false) {
                        "${'"'}"
                            assert(str.length >= 1 && str[0] == 'x')
                                   |   |      |
                                   |   |      ${'$'}tmp3
                                   |   ${'$'}tmp2
                                   ${'$'}tmp1
                        "${'"'}".trimIndent()
                    }
                }
            """.trimIndent(),
        )

        val m by tag("message")
        val finalExample = """
            val tmp1 = str
            val tmp2 = tmp1.length
            val tmp3 = tmp2 >= 1
            when {
                tmp3 -> {
                    val tmp4 = str
                    val tmp5 = tmp4[0]
                    val tmp6 = tmp5 == 'x'
                    assert(tmp6) {$m
                        "${'"'}"
                            assert(str.length >= 1 && str[0] == 'x')
                                   |   |      |       |  |   |
                                   |   |      |       |  |   ${'$'}tmp6
                                   |   |      |       |  ${'$'}tmp5
                                   |   |      |       ${'$'}tmp4
                                   |   |      ${'$'}tmp3
                                   |   ${'$'}tmp2
                                   ${'$'}tmp1
                        "${'"'}".trimIndent()
                    $m}
                }
                else -> assert(false) {$m
                    "${'"'}"
                        assert(str.length >= 1 && str[0] == 'x')
                               |   |      |
                               |   |      ${'$'}tmp3
                               |   ${'$'}tmp2
                               ${'$'}tmp1
                    "${'"'}".trimIndent()
                $m}
            }
        """.trimIndent()

        val transformation = samples.map { it.toCodeSample() }
            .then { finalExample.toCodeSample() }
            .then { collapse(m) }
        transformation +
                transformation[0] +
                transformation[transformation.lastIndex] +
                transformation[transformation.lastIndex - 1]
    }

    carouselScene(frames = samples) {
        SceneCodeSample {
            Box(Modifier.fillMaxSize()) {
                MagicText(transition.createChildTransition { it.toValue().string })
            }
        }
    }
}
