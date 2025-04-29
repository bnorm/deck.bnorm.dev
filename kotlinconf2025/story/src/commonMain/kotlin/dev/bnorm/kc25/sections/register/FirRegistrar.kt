package dev.bnorm.kc25.sections.register

import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
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
import dev.bnorm.kc25.template.INTELLIJ_DARK_CODE_STYLE
import dev.bnorm.kc25.template.StageScaffold
import dev.bnorm.kc25.template.code.CodeSample
import dev.bnorm.kc25.template.code.buildCodeSamples
import dev.bnorm.kc25.template.code1
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedBounds
import dev.bnorm.storyboard.easel.sharedElement
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.text.splitByTags
import dev.bnorm.storyboard.toState

private val SAMPLES = buildCodeSamples {
    val sup by tag("super class")
    val body by tag("class body")
    val sig by tag("configurePlugin signature")
    val dge by tag("declaration generation extension")
    val ace by tag("additional checkers extension")
    val sge by tag("supertype generation extension")
    val ste by tag("status transformer extension")

    // TODO start with the factory version and transform into the method reference version?
    // TODO talk about the different extensions available or just list them all?
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

    // TODO is there a better way to do this transition?
    //  - maybe similar to FIR tree where it's part of the diagram?
    val name = CodeSample(AnnotatedString("FirExtensionRegistrar"))
    name
        .then { baseSample.collapse(body).hide(dge, ace, sge, ste) }
        .then { reveal(body).focus(sig) }
        .then { reveal(dge).focus(dge) }
        .then { reveal(sge).focus(sge) }
        .then { reveal(ste).focus(ste) }
        .then { reveal(ace).focus(ace) }
        .then { unfocus() }
        .then { name }
}

fun StoryboardBuilder.FirRegistrar(sink: MutableList<CodeSample>, start: Int = 0, endExclusive: Int = SAMPLES.size) {
    require(start < endExclusive) { "start=$start must be less than endExclusive=$endExclusive" }
    require(start >= 0) { "start=$start must be greater than or equal to 0" }
    require(endExclusive <= SAMPLES.size) { "end must be less than or equal to ${SAMPLES.size}" }
    sink.addAll(SAMPLES)

    scene(
        stateCount = endExclusive - start,
        enterTransition = { _ -> fadeIn(tween(250, delayMillis = 500, easing = EaseIn)) },
        exitTransition = { _ -> fadeOut(tween(250, easing = EaseOut)) },
    ) {
        StageScaffold { padding ->
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
                    val text = frame.createChildTransition {
                        SAMPLES[start + it.toState()].string.splitByTags()
                    }
                    MagicText(
                        text,
                        modifier = Modifier
                            .sharedBounds(
                                rememberSharedContentState("text:${Component.FirExtensionRegistrar}"),
                                boundsTransform = TextMovementSpec,
                                zIndexInOverlay = -1f,
                            )
                    )
                }
            }
        }
    }
}
