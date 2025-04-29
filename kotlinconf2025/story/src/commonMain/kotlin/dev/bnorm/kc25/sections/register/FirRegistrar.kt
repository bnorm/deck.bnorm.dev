package dev.bnorm.kc25.sections.register

import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.kc25.sections.stages.CompilerStage
import dev.bnorm.kc25.template.INTELLIJ_DARK_CODE_STYLE
import dev.bnorm.kc25.template.StageScaffold
import dev.bnorm.kc25.template.code.CodeSample
import dev.bnorm.kc25.template.code.buildCodeSamples
import dev.bnorm.kc25.template.code1
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedElement
import dev.bnorm.storyboard.map
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.text.splitByTags
import dev.bnorm.storyboard.toState

private val SAMPLES_WITH_TRANSITION = buildCodeSamples {
    val sup by tag("super class")
    val body by tag("class body")
    val sig by tag("configurePlugin signature")
    val dge by tag("declaration generation extension")
    val ace by tag("additional checkers extension")
    val sge by tag("supertype generation extension")
    val ste by tag("status transformer extension")

    val baseSample = """
        class MyFirExtensionRegistrar : ${sup}FirExtensionRegistrar${sup}() {${body}
          ${sig}override fun ExtensionRegistrarContext.configurePlugin()${sig} {${dge}
            +::MyFirDeclarationGenerationExtension${dge}${sge}
            +MyFirSupertypeGenerationExtension.Factory(session)${sge}${ste}
            +{ session: FirSession -> MyFirStatusTransformerExtension(session) }${ste}${ace}
            +::MyFirAdditionalCheckersExtension${ace}
          }
        ${body}}
    """.trimIndent().toCodeSample(INTELLIJ_DARK_CODE_STYLE)

    val blank = CodeSample(AnnotatedString(""))
    blank
        .then { baseSample.collapse(body).hide(dge, ace, sge, ste) }
        .then { reveal(body).focus(sig) }
        .then { reveal(dge).focus(dge) }
        .then { reveal(sge).focus(sge) }
        .then { reveal(ste).focus(ste) }
        .then { reveal(ace).focus(ace) }
        .then { unfocus() }
        .then { blank }
}

private val SAMPLES = SAMPLES_WITH_TRANSITION.subList(fromIndex = 1, toIndex = SAMPLES_WITH_TRANSITION.size - 1)

fun StoryboardBuilder.FirRegistrar(
    sink: MutableList<CodeSample>,
    start: Int = 0,
    endExclusive: Int = SAMPLES.size,
) {
    require(start < endExclusive) { "start=$start must be less than endExclusive=$endExclusive" }
    require(start >= 0) { "start=$start must be greater than or equal to 0" }
    require(endExclusive <= SAMPLES.size) { "end must be less than or equal to ${SAMPLES.size}" }
    sink.addAll(SAMPLES)

    scene(
        stateCount = endExclusive - start,
        enterTransition = { _ -> fadeIn(tween(250, delayMillis = 500, easing = EaseIn)) },
        exitTransition = { _ -> fadeOut(tween(250, easing = EaseOut)) },
    ) {
        StageScaffold(
            stages = updateTransition(setOf(CompilerStage.Resolve, CompilerStage.Analyze)),
        ) { padding ->
            Box(
                Modifier.padding(padding)
                    .padding(bottom = 32.dp)
                    .sharedElement(
                        rememberSharedContentState("box:${Component.FirExtensionRegistrar}"),
                        boundsTransform = BoxMovementSpec,
                        zIndexInOverlay = -1f,
                    )
                    .fillMaxSize()
                    .border(2.dp, MaterialTheme.colors.secondary, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                ProvideTextStyle(MaterialTheme.typography.code1) {
                    val text = frame.createChildTransition { frame ->
                        val index = frame.map { it + 1 }.toState(start = 0, end = SAMPLES_WITH_TRANSITION.lastIndex)
                        SAMPLES_WITH_TRANSITION[start + index].string.splitByTags()
                    }
                    MagicText(text, modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}
