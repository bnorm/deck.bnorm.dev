package dev.bnorm.kc26.sections.next

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.code.CodeSample
import dev.bnorm.deck.shared.code.MagicCodeSample
import dev.bnorm.deck.shared.code.buildCodeSamples
import dev.bnorm.deck.shared.mac.MacTerminalPopup
import dev.bnorm.kc26.template.CODE_STYLE
import dev.bnorm.kc26.template.SectionSceneScaffold
import dev.bnorm.kc26.template.carouselScene
import dev.bnorm.kc26.template.code1
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.text.highlight.CodeScope
import dev.bnorm.storyboard.toValue

fun StoryboardBuilder.PowerAssertLocalVariables() {
    class SceneState(
        val sample: CodeSample,
        val visible: Boolean,
        val output: CodeSample,
    )

    carouselScene(
        listOf(
            SceneState(LocalVariablePowerAssert[0], false, LocalVariableOutput[0]), // sample
            SceneState(LocalVariablePowerAssert[0], true, LocalVariableOutput[0]), // output
            SceneState(LocalVariablePowerAssert[0], false, LocalVariableOutput[0]), // sample
            SceneState(LocalVariablePowerAssert[1], false, LocalVariableOutput[1]), // new sample
            SceneState(LocalVariablePowerAssert[1], true, LocalVariableOutput[1]), // bad output
            SceneState(LocalVariablePowerAssert[2], true, LocalVariableOutput[1]), // ann sample
            SceneState(LocalVariablePowerAssert[2], true, LocalVariableOutput[2]), // better output
            SceneState(LocalVariablePowerAssert[2], true, LocalVariableOutput[3]), // even better output?
        )
    ) {
        SectionSceneScaffold { padding ->
            ProvideTextStyle(MaterialTheme.typography.code1) {
                MagicCodeSample(
                    sample = transition.createChildTransition { it.toValue().sample },
                    modifier = Modifier.fillMaxSize().padding(padding)
                )
            }
        }

        MacTerminalPopup(visible = transition.createChildTransition { it.toValue().visible }) {
            Box(Modifier.height(256.dp)) {
                ProvideTextStyle(MaterialTheme.typography.code1 + TextStyle(fontFamily = FontFamily.Monospace)) {
                    MagicCodeSample(
                        sample = transition.createChildTransition { it.toValue().output },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

val LocalVariablePowerAssert = buildCodeSamples {
    fun String.toCodeSample(): CodeSample = trimIndent().toCodeSample(
        codeStyle = CODE_STYLE,
        scope = CodeScope.Function,
    ) { identifier ->
        when (identifier) {
            "powerAssert" -> CODE_STYLE.staticFunctionCall
            "trimIndent" -> CODE_STYLE.extensionFunctionCall
            "length" -> CODE_STYLE.property
            else -> null
        }
    }

    val s by tag("splitter")

    listOf(
        """
            powerAssert(${s}"Hello".length${s} == ${s}"World".substring(1, 4).length${s})
        """.toCodeSample(),
        """
            ${s}${s}val expected = ${s}"Hello".length${s}
            ${s}${s}val actual = ${s}"World".substring(1, 4).length${s}
            powerAssert(${s}expected${s} == ${s}actual${s})
        """.toCodeSample(),
        // TODO last two lines are not highlighted correctly... :thinking_face:
        """
            ${s}@PowerAssert ${s}val expected = ${s}"Hello".length${s}
            ${s}@PowerAssert ${s}val actual = ${s}"World".substring(1, 4).length${s}
            powerAssert(${s}expected${s} == ${s}actual${s})
        """.toCodeSample(),
    )
}

val LocalVariableOutput = buildCodeSamples {
    fun String.toCodeSample(): CodeSample = CodeSample(lazy {
        extractTags(trimIndent())
    })

    val s by tag("splitter")

    listOf(
        """
            powerAssert(hello.length == "World".substring(1, 4).length)
                        |     |      |          |               |
                        |     5      false      |               3
                        "Hello"                 "orl"
        """.toCodeSample(),
        """
            powerAssert(expected == actual)
                        |        |  |
                        5        ${s}|${s}  3
                                 ${s}false${s}
        """.toCodeSample(),
        """
            val expected = ${s}"Hello".length${s}
                                   ${s}|${s}
                                   ${s}5${s}
            val actual = ${s}"World".substring(1, 4).length${s}
                                 ${s}|${s}               ${s}|${s}
                                 ${s}orl${s}             ${s}3${s}
            powerAssert(expected == actual)
                        |        |  |
                        5        ${s}|${s}  3
                                 ${s}false${s}
        """.toCodeSample(),
        """
            powerAssert(expected == actual)
                        |        |  |
                        |        ${s}|${s}  ${s}"World".substring(1, 4).length${s}
                        |        ${s}false${s}      ${s}|${s}               ${s}|${s}
                        ${s}"Hello".length${s}      ${s}orl${s}             ${s}3${s}
                                ${s}|${s}
                                ${s}5${s}
        """.toCodeSample(),
    )
}