package dev.bnorm.kc26.sections

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.ResourceImage
import dev.bnorm.deck.shared.code.CodeSample
import dev.bnorm.deck.shared.code.buildCodeSamples
import dev.bnorm.deck.story.generated.resources.Res
import dev.bnorm.deck.story.generated.resources.qr_power_assert_keep
import dev.bnorm.kc26.components.GradientText
import dev.bnorm.kc26.components.OutputComparison
import dev.bnorm.kc26.components.SceneCodeSample
import dev.bnorm.kc26.template.CODE_STYLE
import dev.bnorm.kc26.template.carouselScene
import dev.bnorm.kc26.template.code2
import dev.bnorm.kc26.template.toKotlin
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.mapToValue
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.text.splitByTags
import dev.bnorm.storyboard.toValue

fun StoryboardBuilder.MajorSection() {
    // TICKETS:
    // https://youtrack.jetbrains.com/issue/KT-75266/PowerAssert-arrayOf-isnt-displayed-on-the-diagram
    // https://youtrack.jetbrains.com/issue/KT-69036/Power-Assert-indent-multiline-values

    CallExplanationSolution()
    ArrayProblem()
    MultilineProblem()
    AnnotationTransformation()
    AnnotationUse()
}

private fun StoryboardBuilder.ArrayProblem() {
    val arraySample = """
        @Test fun test() {
            assert(arrayOf(true, false)[1])
        }
    """.trimIndent().toKotlin {
        when (it) {
            "assert", "arrayOf" -> staticFunctionCall
            else -> null
        }
    }

    val arrayOutput = buildCodeSamples {
        fun String.toCodeSample(): CodeSample = CodeSample(lazy {
            extractTags(trimIndent())
        })

        val s by tag("splitter")

        listOf(
            """
                assert(arrayOf(true, false)[1])
                       |                   |
                       |                   false
                       [Ljava.lang.Boolean;@47caedad
            """.toCodeSample(),
            """
                assert(arrayOf(true, false)[1])
                       |                   |
                       |                   ${s}false${s}
                       ${s}[true, false]${s}
            """.toCodeSample(),
            """
                assert(arrayOf(true, false)[1])
                       |                   |
                       ${s}[true, false]${s}       ${s}false${s}
            """.toCodeSample(),
        )
    }

    data class SceneState(
        val showOutput: Boolean,
        val showAfter: Boolean,
        val afterOutput: CodeSample,
    )

    carouselScene(
        frames = listOf(
            SceneState(showOutput = false, showAfter = false, afterOutput = arrayOutput[1]),
            SceneState(showOutput = true, showAfter = false, afterOutput = arrayOutput[1]),
            SceneState(showOutput = true, showAfter = true, afterOutput = arrayOutput[1]),
            SceneState(showOutput = true, showAfter = true, afterOutput = arrayOutput[2]),
        ),
    ) {
        Column {
            SceneCodeSample(
                content = { Text(arraySample) },
                output = {
                    OutputComparison(
                        beforeVersion = { GradientText("2.0") },
                        beforeOutput = { Text(arrayOutput[0].string) },
                        afterVersion = { GradientText("2.4") },
                        afterOutput = {
                            MagicText(transition.createChildTransition { it.toValue().afterOutput.string.splitByTags() })
                        },
                        showAfter = transition.createChildTransition { frame ->
                            frame.mapToValue(start = false, end = true) { it.showAfter }
                        },
                    )
                },
                hideOutput = transition.createChildTransition { frame ->
                    frame.mapToValue(start = true, end = true) { !it.showOutput }
                },
            )
        }
    }
}

private fun StoryboardBuilder.MultilineProblem() {
    val stringSample = """
        @Test fun test() {
            val a = "another string"
            val s = "this is a multiline\nstring. this is the second line"
            assert(a == s)
        }
    """.trimIndent().toKotlin {
        when (it) {
            "assert" -> staticFunctionCall
            else -> null
        }
    }

    val stringOutput = buildCodeSamples {
        fun String.toCodeSample(): CodeSample = CodeSample(lazy {
            extractTags(trimIndent())
        })

        val s by tag("splitter")

        listOf(
            """
                assert(a == s)
                       | |  |
                       | |  this is a multiline
                string. this is the second line
                       | false
                       another string
            """.toCodeSample(),
            """
                assert(a == s)
                       | |  |
                       | |  this is a multiline
                       | |  string. this is the second line
                       | false
                       ${s}another string${s}
            """.toCodeSample(),
            """
                assert(a == s)
                       | |  |
                       | |  ""${'"'}
                       | |  this is a multiline
                       | |  string. this is the second line
                       | |  ""${'"'}
                       | false
                       "${s}another string${s}"
            """.toCodeSample(),
        )
    }

    data class SceneState(
        val showOutput: Boolean,
        val showAfter: Boolean,
        val afterOutput: CodeSample,
    )

    carouselScene(
        frames = listOf(
            SceneState(showOutput = false, showAfter = false, afterOutput = stringOutput[1]),
            SceneState(showOutput = true, showAfter = false, afterOutput = stringOutput[1]),
            SceneState(showOutput = true, showAfter = true, afterOutput = stringOutput[1]),
            SceneState(showOutput = true, showAfter = true, afterOutput = stringOutput[2]),
        ),
    ) {
        Column {
            SceneCodeSample(
                content = { Text(stringSample) },
                output = {
                    OutputComparison(
                        beforeVersion = { GradientText("2.0") },
                        beforeOutput = { Text(stringOutput[0].string) },
                        afterVersion = { GradientText("2.4") },
                        afterOutput = {
                            MagicText(transition.createChildTransition { it.toValue().afterOutput.string.splitByTags() })
                        },
                        showAfter = transition.createChildTransition { frame ->
                            frame.mapToValue(start = false, end = true) { it.showAfter }
                        },
                    )
                },
                hideOutput = transition.createChildTransition { frame ->
                    frame.mapToValue(start = true, end = true) { !it.showOutput }
                },
            )
        }
    }
}

private fun StoryboardBuilder.CallExplanationSolution() {
    val samples = buildCodeSamples {
        fun String.toCodeSample(): CodeSample = trimIndent().toCodeSample(CODE_STYLE) { identifier ->
            when (identifier) {
                "assert" -> CODE_STYLE.staticFunctionCall
                "trimIndent", "toDefaultMessage" -> CODE_STYLE.extensionFunctionCall
                "length" -> CODE_STYLE.property
                else -> null
            }
        }

        val e by tag("explanation")
        val m by tag("default message")

        val start = """
            val tmp1 = str
            val tmp2 = tmp1.length
            val tmp3 = tmp2 >= 1
            when {
                tmp3 -> {
                    val tmp4 = str
                    val tmp5 = tmp4[0]
                    val tmp6 = tmp5 == 'x'
                    assert(tmp6) {$e
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
                    $e}
                }
                else -> assert(false) {$e
                    "${'"'}"
                        assert(str.length >= 1 && str[0] == 'x')
                               |   |      |
                               |   |      ${'$'}tmp3
                               |   ${'$'}tmp2
                               ${'$'}tmp1
                    "${'"'}".trimIndent()
                $e}
            }
        """.toCodeSample()

        val end = """
            val tmp1 = str
            val tmp2 = tmp1.length
            val tmp3 = tmp2 >= 1
            when {
                tmp3 -> {
                    val tmp4 = str
                    val tmp5 = tmp4[0]
                    val tmp6 = tmp5 == 'x'
                    assert(tmp6) {$e
                        CallExplanation(/* ... */)${m}.toDefaultMessage()${m}
                    $e}
                }
                else -> assert(false) {$e
                    CallExplanation(/* ... */)${m}.toDefaultMessage()${m}
                $e}
            }
        """.toCodeSample()

        start.collapse(e)
            .then { reveal(e) }
            .then { end }
    }

    carouselScene(frames = samples) {
        SceneCodeSample {
            Box(Modifier.fillMaxSize()) {
                MagicText(transition.createChildTransition { it.toValue().string.splitByTags() })
            }
        }
    }
}

private fun StoryboardBuilder.AnnotationTransformation() {
    val originalFun = buildCodeSamples {
        fun String.toCodeSample(): CodeSample {
            return CodeSample(lazy {
                var count = 0
                extractTags(this).toKotlin {
                    when (it) {
                        "PowerAssert" -> CODE_STYLE.annotation
                        "explanation" if count == 0 -> CODE_STYLE.staticProperty.also { count++ } // TODO weird hack to avoid local property coloring
                        "expressions", "size", "rhs", "lhs", "value" -> CODE_STYLE.property
                        "filterIsInstance", "filter", "toDefaultMessage", "map" -> CODE_STYLE.extensionFunctionCall
                        else -> null
                    }
                }
            })
        }

        val exp by tag("explanation call")
        val c by tag("collapse")
        val f by tag("failures")
        val t by tag("throw")

        val transformed = """
                @PowerAssert fun powerAssert(condition: Boolean) {
                    if (!condition) {
                        val explanation: CallExplanation? = ${exp}null${exp}
                            ?: throw AssertionError("Assertion failed")
                        ${c}// ...${c}
                    }
                }
            """.trimIndent().toCodeSample().collapse(c)

        val sample = """
                @PowerAssert fun powerAssert(condition: Boolean) {
                    if (!condition) {
                        val explanation: CallExplanation? = ${exp}PowerAssert.explanation${exp}
                            ?: throw AssertionError("Assertion failed")
                        ${c}
                        ${f}val failures = explanation.expressions
                            .filterIsInstance<EqualityExpression>()
                            .filter { it.value == false }${f}
                
                        ${t}val message = explanation.toDefaultMessage()
                        throw when (failures.size) {
                            0 -> AssertionFailedError(message)
                            1 -> AssertionFailedError(message, failures[0].rhs, failures[0].lhs)
                            else -> MultipleFailuresError(
                                heading = message, failures = failures.map { 
                                    AssertionFailedError(message = null, it.rhs, it.lhs)
                                }
                            )
                        }${t}${c}
                    }
                }
            """.trimIndent().toCodeSample()

        listOf(
            sample.collapse(c),
            transformed,
            transformed.focus(c),
            sample.collapse(c),
            sample.focus(f),
            sample.focus(t),
        )
    }

    val syntheticFun = buildCodeSamples {
        fun String.toCodeSample(): CodeSample {
            return CodeSample(lazy {
                extractTags(this).toKotlin(identifierStyle = {
                    when (it) {
                        "explanation" -> CODE_STYLE.staticProperty
                        "PowerAssert" -> CODE_STYLE.annotation
                        else -> null
                    }
                })
            })
        }

        val c by tag("collapse")
        val h by tag("hide")
        val exp by tag("explanation call")
        val invoke by tag("explanation call")
        val name by tag("explanation call")
        val ann by tag("explanation call")
        val param by tag("explanation call")

        val base = """
        ${ann}@PowerAssert ${ann}fun ${name}`${name}powerAssert${name}${'$'}powerassert`${name}($param
            ${param}condition: Boolean${param},
            `${'$'}explanation`: () -> CallExplanation,
        ${param}) {
            if (!condition) {
                val explanation: CallExplanation? = ${exp}PowerAssert.explanation${exp}${h} ?: ${h}${invoke}`${'$'}explanation`.invoke()${invoke}
                    ?: throw AssertionError("Assertion failed")
                ${c}// ...${c}
            }
        }
    """.trimIndent().toCodeSample()

        base.hide(h, name, param, invoke).collapse(c).focus(h)
            .then { unfocus() }
            .then { reveal(name) }
            .then { hide(ann) }
            .then { reveal(param) }
            .then { hide(exp).reveal(invoke) }
            .then { focus(h) }
    }

    @Composable
    fun OriginalFunction(transition: Transition<Int>, modifier: Modifier = Modifier) {
        Box(modifier) {
            ProvideTextStyle(MaterialTheme.typography.code2) {
                MagicText(transition.createChildTransition {
                    originalFun[it].string.splitByTags()
                })
            }
        }
    }

    @Composable
    fun SyntheticFunction(transition: Transition<Int>, modifier: Modifier = Modifier) {
        Box(modifier) {
            ProvideTextStyle(MaterialTheme.typography.code2) {
                MagicText(transition.createChildTransition {
                    syntheticFun[it].string.splitByTags()
                })
            }
        }
    }

    data class SceneState(
        val original: Int,
        val synthetic: Int,
        val showSynthetic: Boolean,
    )

    carouselScene(
        start = SceneState(original = 0, synthetic = 0, showSynthetic = false),
        frames = {
            next { copy(showSynthetic = true) }
            next { copy(original = original + 1) }

            before { copy(original = original + 1) }
            while (current.synthetic + 2 < syntheticFun.size) {
                next { copy(synthetic = synthetic + 1) }
            }
            next { copy(original = original + 1, showSynthetic = false) }
            while (current.original + 1 < originalFun.size) {
                next { copy(original = original + 1) }
            }
        },
    ) {
        fun <T> spec() = tween<T>(500, easing = EaseInOut)

        SceneCodeSample {
            Box(Modifier.fillMaxSize()) {
                val original = transition.createChildTransition { it.toValue().original }
                val synthetic = transition.createChildTransition { it.toValue().synthetic }
                Column(Modifier.fillMaxSize()) {
                    OriginalFunction(transition = original)
                    Spacer(Modifier.size(16.dp))
                    transition.AnimatedVisibility(
                        visible = { it.toValue().showSynthetic },
                        enter = slideInVertically(spec()) { -252 - 32 } + fadeIn(spec()),
                        exit = slideOutVertically(spec()) { -252 - 32 } + fadeOut(spec()),
                    ) {
                        SyntheticFunction(transition = synthetic)
                    }
                }

                ResourceImage(
                    Res.drawable.qr_power_assert_keep,
                    modifier = Modifier.align(Alignment.BottomEnd).height(120.dp)
                )
            }
        }
    }
}

private fun StoryboardBuilder.AnnotationUse() {
    val arraySample = """
        val hello = "Hello"
        powerAssert(hello.length == "World".substring(1, 4).length)
    """.trimIndent().toKotlin {
        when (it) {
            "substring" -> extensionFunctionCall
            "length" -> property
            "powerAssert" -> staticFunctionCall
            else -> null
        }
    }

    val arrayOutput = buildCodeSamples {
        fun String.toCodeSample(): CodeSample = CodeSample(lazy {
            extractTags(trimIndent())
        })

        listOf(
            """
                com.willowtreeapps.opentest4k.AssertionFailedError:
                powerAssert(hello.length == "World".substring(1, 4).length)
                            |     |      |          |               |
                            |     5      false      "orl"           3
                            "Hello"
            """.toCodeSample(),
            """
                com.willowtreeapps.opentest4k.AssertionFailedError:
                powerAssert(hello.length == "World".substring(1, 4).length)
                            |     |      |          |               |
                            |     5      false      "orl"           3
                            "Hello"

                Expected :3
                Actual   :5
                <Click to see difference>
            """.toCodeSample(),
        )
    }

    data class SceneState(
        val showOutput: Boolean,
        val afterOutput: CodeSample,
    )

    carouselScene(
        frames = listOf(
            SceneState(showOutput = false, afterOutput = arrayOutput[0]),
            SceneState(showOutput = true, afterOutput = arrayOutput[0]),
            SceneState(showOutput = true, afterOutput = arrayOutput[1]),
        ),
    ) {
        SceneCodeSample(
            content = {
                Text(arraySample)
            },
            output = {
                OutputComparison(
                    beforeVersion = {},
                    beforeOutput = {},
                    afterVersion = { GradientText("2.4") },
                    afterOutput = {
                        MagicText(transition.createChildTransition { it.toValue().afterOutput.string.splitByTags() })
                    },
                    showAfter = updateTransition(true),
                )
            },
            hideOutput = transition.createChildTransition { frame ->
                frame.mapToValue(start = true, end = true) { !it.showOutput }
            },
        )
    }
}
