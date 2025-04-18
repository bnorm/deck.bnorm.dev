package dev.bnorm.kc25.sections.write.resolve

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.kc25.components.MagicCodeSample
import dev.bnorm.kc25.components.RightPanel
import dev.bnorm.kc25.sections.stages.CompilerStage
import dev.bnorm.kc25.template.INTELLIJ_DARK_CODE_STYLE
import dev.bnorm.kc25.template.StageScaffold
import dev.bnorm.kc25.template.code.CodeSample
import dev.bnorm.kc25.template.code.buildCodeSamples
import dev.bnorm.kc25.template.code.toCode
import dev.bnorm.kc25.template.code1
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.text.splitByTags
import dev.bnorm.storyboard.toState

private sealed class SampleData {
    data object Signature : SampleData()
    data object Parameters : SampleData()
    data object Body : SampleData()
    data object Spacing : SampleData()

}

class ShowPanel(
    val sample: CodeSample,
    val show: Boolean,
)

private val BuilderClassKey = """
    data class BuilderClassKey(
      val ownerClassSymbol: FirClassSymbol<*>,
      val constructorSymbol: FirConstructorSymbol,
    ) : GeneratedDeclarationKey()
""".trimIndent().toCode(INTELLIJ_DARK_CODE_STYLE)

private val SAMPLES = buildCodeSamples {
    val clsSig by tag("class signature")
    val funName by tag("function names")

    val cob by tag("companion object body", SampleData.Body)

    val co_rp by tag("companion object <-> registerPredicates", SampleData.Spacing)

    val rpb by tag("registerPredicates body", SampleData.Body)
    val rps by tag("registerPredicates signature", SampleData.Signature)

    val rp_nest by tag("registerPredicates <-> getNestedClassifiersNames", SampleData.Spacing)

    val nests by tag("getNestedClassifiersNames signature", SampleData.Signature)
    val nestp by tag("getNestedClassifiersNames param", SampleData.Parameters)
    val nestb by tag("getNestedClassifiersNames body", SampleData.Body)

    val nest_class by tag("getNestedClassifiersNames <-> generateNestedClassLikeDeclaration", SampleData.Spacing)

    val classs by tag("generateNestedClassLikeDeclaration signature", SampleData.Signature)
    val classp by tag("generateNestedClassLikeDeclaration param", SampleData.Parameters)
    val classb by tag("generateNestedClassLikeDeclaration body", SampleData.Body)
    val class1 by tag("generateNestedClassLikeDeclaration focus 1")
    val class2 by tag("generateNestedClassLikeDeclaration focus 2")
    val class3 by tag("generateNestedClassLikeDeclaration focus 3")

    val class_call by tag("generateNestedClassLikeDeclaration <-> getCallableNamesForClass", SampleData.Spacing)

    val calls by tag("getCallableNamesForClass signature", SampleData.Signature)
    val callp by tag("getCallableNamesForClass param", SampleData.Parameters)
    val callb by tag("getCallableNamesForClass body", SampleData.Body)

    val call_ctor by tag("getCallableNamesForClass <-> generateConstructors", SampleData.Spacing)

    val ctors by tag("generateConstructors signature", SampleData.Signature)
    val ctorp by tag("generateConstructors param", SampleData.Parameters)
    val ctorb by tag("generateConstructors body", SampleData.Body)

    val ctor_props by tag("generateConstructors <-> generateProperties", SampleData.Spacing)

    val props by tag("generateProperties signature", SampleData.Signature)
    val propp by tag("generateProperties param", SampleData.Parameters)
    val propb by tag("generateProperties body", SampleData.Body)

    val props_fun by tag("generateProperties <-> generateFunctions", SampleData.Spacing)

    val funs by tag("generateFunctions signature", SampleData.Signature)
    val funp by tag("generateFunctions param", SampleData.Parameters)
    val funb by tag("generateFunctions body", SampleData.Body)

    val base = """
        ${clsSig}class BuildableFirDeclarationGenerationExtension(
          session: FirSession,
        ) : FirDeclarationGenerationExtension(session)${clsSig} {
          companion object {${cob}
            val BUILDER_CLASS_NAME = Name.identifier("Builder")
            val BUILD_FUN_NAME = Name.identifier("build")

            private val BUILDABLE_PREDICATE = LookupPredicate.create {
              annotated(FqName("dev.bnorm.buildable.Buildable"))
            }

            private val HAS_BUILDABLE_PREDICATE = LookupPredicate.create {
              hasAnnotated(FqName("dev.bnorm.buildable.Buildable"))
            }
          ${cob}}
          ${co_rp}
          ${co_rp}${rps}override fun FirDeclarationPredicateRegistrar.${funName}registerPredicates${funName}()${rps} {${rpb}
            register(BUILDABLE_PREDICATE)
            register(HAS_BUILDABLE_PREDICATE)
          ${rpb}}${rp_nest}
          ${rp_nest}
          ${nests}override fun ${funName}getNestedClassifiersNames${funName}(${nestp}
            classSymbol: FirClassSymbol<*>,
            context: NestedClassGenerationContext,
          ${nestp}): Set<Name>${nests} {${nestb}
            val provider = session.predicateBasedProvider
            if (!provider.matches(HAS_BUILDABLE_PREDICATE, classSymbol))
              return emptySet()

            return setOf(BUILDER_CLASS_NAME)
          ${nestb}}
          ${nest_class}
          ${nest_class}${classs}override fun ${funName}generateNestedClassLikeDeclaration${funName}(${classp}
            owner: FirClassSymbol<*>,
            name: Name,
            context: NestedClassGenerationContext,
          ${classp}): FirClassLikeSymbol<*>?${classs} {${classb}${class1}
            if (name != BUILDER_CLASS_NAME) return null${class1}

            ${class2}// ??? TODO should we be using owner.declarationSymbols instead?
            val scope: FirScope =
              owner.declaredMemberScope(session, memberRequiredPhase = null)
            val provider = session.predicateBasedProvider
            val constructorSymbol = scope.getDeclaredConstructors()
              .singleOrNull { provider.matches(BUILDABLE_PREDICATE, it) }
              ?: return null${class2}

            ${class3}val builderClass = createNestedClass(
              owner = owner,
              name = BUILDER_CLASS_NAME,
              key = BuilderClassKey(owner, constructorSymbol),
            ) {
              visibility = constructorSymbol.visibility
                .takeIf { it != Visibilities.Unknown }
                ?: owner.visibility
            }

            return builderClass.symbol${class3}
          ${classb}}
          ${class_call}
          ${class_call}${calls}override fun ${funName}getCallableNamesForClass${funName}(${callp}
            classSymbol: FirClassSymbol<*>,
            context: MemberGenerationContext,
          ${callp}): Set<Name>${calls} {${callb}
            val key = (classSymbol.origin as? FirDeclarationOrigin.Plugin)?.key
            if (key !is BuilderClassKey) return emptySet()

            return buildSet {
              add(SpecialNames.INIT)
              addAll(key.constructorSymbol.valueParameterSymbols.map { it.name })
              add(BUILD_FUN_NAME)
            }
          ${callb}}
          ${call_ctor}
          ${call_ctor}${ctors}override fun ${funName}generateConstructors${funName}(${ctorp}
            context: MemberGenerationContext,
          ${ctorp}): List<FirConstructorSymbol>${ctors} {${ctorb}
            val builderClassSymbol = context.owner
            val key = (builderClassSymbol.origin as? FirDeclarationOrigin.Plugin)?.key
            if (key !is BuilderClassKey) return emptyList()

            val constructor = createConstructor(
              owner = builderClassSymbol,
              key = BuildableKey,
              isPrimary = true,
            )

            return listOf(constructor.symbol)
          ${ctorb}}
          ${ctor_props}
          ${ctor_props}${props}override fun ${funName}generateProperties${funName}(${propp}
            callableId: CallableId,
            context: MemberGenerationContext?,
          ${propp}): List<FirPropertySymbol>${props} {${propb}
            val builderClassSymbol = context?.owner
            val key = (builderClassSymbol?.origin as? FirDeclarationOrigin.Plugin)?.key
            if (key !is BuilderClassKey) return emptyList()

            val parameterSymbol = key.constructorSymbol.valueParameterSymbols
              .singleOrNull { it.name == callableId.callableName }
              ?: return emptyList()

            val property = createMemberProperty(
              owner = builderClassSymbol,
              key = BuildableKey,
              name = parameterSymbol.name,
              returnType = parameterSymbol.resolvedReturnType,
              isVal = false,
              hasBackingField = false,
            )

            return listOf(property.symbol)
          ${propb}}
          ${props_fun}
          ${props_fun}${funs}override fun ${funName}generateFunctions${funName}(${funp}
            callableId: CallableId,
            context: MemberGenerationContext?,
          ${funp}): List<FirNamedFunctionSymbol>${funs} {${funb}
            if (callableId.callableName != BUILD_FUN_NAME)
              return emptyList()

            val builderClassSymbol = context?.owner
            val key = (builderClassSymbol?.origin as? FirDeclarationOrigin.Plugin)?.key
            if (key !is BuilderClassKey) return emptyList()

            val build = createMemberFunction(
              owner = builderClassSymbol,
              key = BuildableKey,
              name = callableId.callableName,
              returnType = key.ownerClassSymbol.constructType(),
            )

            return listOf(build.symbol)
          ${funb}}
        }
    """.trimIndent().toCodeSample(INTELLIJ_DARK_CODE_STYLE)

    val start = base.collapse(SampleData.Body)
        .collapse(SampleData.Parameters)
        .hide(SampleData.Spacing)

    start.focus(clsSig)
        .then { focus(funName, scroll = false) }

        .then { reveal(cob, co_rp).focus(cob) }

        .then { start.reveal(co_rp, rp_nest).focus(rps) }
        .then { reveal(rpb).focus(rpb) }

        .then { start.reveal(nestp, rp_nest, nest_class).focus(nests) }
        .then { collapse(nestp).reveal(nestb).focus(nestb) }

        .then { start.reveal(classp, nest_class, class_call).focus(classs) }
        .then { collapse(classp).reveal(classb).focus(class1) }
        .then { focus(class2) }
        .then { focus(class3).attach(ShowPanel(CodeSample(BuilderClassKey), show = false)) }
        .then { attach(ShowPanel(CodeSample(BuilderClassKey), show = true)) }
        .then { attach(ShowPanel(CodeSample(BuilderClassKey), show = false)) }

        .then { start.reveal(callp, class_call, call_ctor).focus(calls) }
        .then { collapse(callp).reveal(callb).focus(callb) }

        .then { start.reveal(ctorp, call_ctor, ctor_props).focus(ctors) }
        .then { collapse(ctorp).reveal(ctorb).focus(ctorb) }

        .then { start.reveal(propp, ctor_props, props_fun).focus(props) }
        .then { collapse(propp).reveal(propb).focus(propb) }

        .then { start.reveal(funp, props_fun).focus(funs) }
        .then { collapse(funp).reveal(funb).focus(funb) }

        .then { start }
}

fun StoryboardBuilder.Generation(start: Int = 0, endExclusive: Int = SAMPLES.size) {
    require(start < endExclusive) { "start=$start must be less than endExclusive=$endExclusive" }
    require(start >= 0) { "start=$start must be greater than or equal to 0" }
    require(endExclusive <= SAMPLES.size) { "end must be less than or equal to ${SAMPLES.size}" }

    scene(
        stateCount = endExclusive - start,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        val sample = frame.createChildTransition { SAMPLES[start + it.toState()] }

        StageScaffold(updateTransition(CompilerStage.Resolve)) { padding ->
            ProvideTextStyle(MaterialTheme.typography.code1) {
                sample.MagicCodeSample(modifier = Modifier.padding(padding))
            }

            val sidePanel = sample.createChildTransition { (it.data as? ShowPanel) }
            RightPanel(
                show = sidePanel.createChildTransition { it != null && it.show },
                modifier = Modifier.padding(top = padding.calculateTopPadding()),
            ) {
                ProvideTextStyle(MaterialTheme.typography.code1) {
                    Box(modifier = Modifier.padding(32.dp).width(500.dp).fillMaxHeight()) {
                        MagicText(sidePanel.createChildTransition { it?.sample?.string?.splitByTags() ?: emptyList() })
                    }
                }
            }
        }
    }
}

