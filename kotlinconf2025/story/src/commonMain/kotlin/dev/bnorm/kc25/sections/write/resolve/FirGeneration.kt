package dev.bnorm.kc25.sections.write.resolve

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import dev.bnorm.kc25.components.validateSample
import dev.bnorm.kc25.sections.stages.CompilerStage
import dev.bnorm.kc25.template.RightPanel
import dev.bnorm.kc25.template.StageSampleScene
import dev.bnorm.kc25.template.code.CodeSample
import dev.bnorm.kc25.template.code.buildCodeSamples
import dev.bnorm.kc25.template.code.toCode
import dev.bnorm.storyboard.StoryboardBuilder

private sealed class SampleData {
    data object Signature : SampleData()
    data object Parameters : SampleData()
    data object Body : SampleData()
}

private val BuilderClassKey = CodeSample(
    """
        data class BuilderClassKey(
          val ownerClassSymbol: FirClassSymbol<*>,
          val constructorSymbol: FirConstructorSymbol,
        ) : GeneratedDeclarationKey()
    """.trimIndent().toCode()
)

private val BUILDER_CLASS_KEY_PANEL = RightPanel(0, listOf(BuilderClassKey), show = false)

private val BUILDABLE = buildCodeSamples {
    val sample by tag("")
    val eClass by tag("")
    val eCtor by tag("")
    val eProp by tag("")
    val eCall by tag("")

    val ctor by tag("book constructor")
    val builder by tag("book builder")
    val bctor by tag("book builder constructor")
    val properties by tag("book builder properties")
    val functions by tag("book builder functions")

    val bookSample = """
        ${sample}fun main() {
          val book = Book.${eClass}Builder${eClass}${eCtor}()${eCtor}.apply {
            ${eProp}title${eProp} = "The Fellowship of the Ring"
            ${eProp}series${eProp} = "The Lord of the Rings"
            ${eProp}author${eProp} = "J. R. R. Tolkien"
            ${eProp}publication${eProp} = LocalDate(year = 1954, Month.JULY, dayOfMonth = 2)
          }.${eCall}build()${eCall}
        }
        
        ${sample}class Book @Buildable constructor(${ctor}
          val title: String,
          val series: String? = null,
          val author: String,
          val publication: LocalDate,
        ${ctor})${builder} {
          class Builder${bctor} constructor()${bctor}${properties} {
            var title: String
            var series: String?
            var author: String
            var publication: LocalDate${functions}
            fun build(): Book${functions}
          }${properties}
        }${builder}
    """.trimIndent().toCodeSample()

    val noErrors = bookSample.hide(builder, bctor, properties, functions).collapse(ctor)
    val callErrors = noErrors
        .styled(eProp, SpanStyle(color = Color.Red))
        .styled(eCall, SpanStyle(color = Color.Red))
        .styled(eCtor, SpanStyle(color = Color.Red))
    val allErrors = callErrors
        .styled(eClass, SpanStyle(color = Color.Red))

    bookSample.hide(sample, builder)
        .then { allErrors }

        // Nested classes
        .then { styled(eClass, SpanStyle(color = Color.Yellow)) }
        .then { callErrors.reveal(builder) }

        // Callables
        .then {
            // Callable names "yellow"
            styled(eCtor, SpanStyle(color = Color.Yellow))
                .styled(eProp, SpanStyle(color = Color.Yellow))
                .styled(eCall, SpanStyle(color = Color.Yellow))
        }
        .then {
            // Constructor "green"
            noErrors
                .styled(eProp, SpanStyle(color = Color.Yellow))
                .styled(eCall, SpanStyle(color = Color.Yellow))
                .reveal(builder, bctor)
        }
        .then {
            // Constructor and properties "green"
            noErrors
                .styled(eCall, SpanStyle(color = Color.Yellow))
                .reveal(builder, bctor, properties)
        }
        .then {
            // Constructor, properties, and function "green"
            noErrors.reveal(builder, bctor, properties, functions)
        }
}

private val BOOK_PANEL = RightPanel(0, BUILDABLE, show = false)
private val BUILDER_CLASS_NAME_PANEL = RightPanel(1, BUILDABLE, show = false)
private val BUILDER_CLASS_SYMBOL_PANEL = RightPanel(2, BUILDABLE, show = false)
private val BUILDER_CALL_NAME_PANEL = RightPanel(3, BUILDABLE, show = false)
private val BUILDER_CONSTRUCTOR_PANEL = RightPanel(4, BUILDABLE, show = false)
private val BUILDER_PROPERTY_PANEL = RightPanel(5, BUILDABLE, show = false)
private val BUILDER_FUNCTION_PANEL = RightPanel(6, BUILDABLE, show = false)

private val VALIDATE_SAMPLES = buildCodeSamples {
    val clsSig by tag("class signature")
    val funName by tag("function names")

    val cob by tag("companion object body", SampleData.Body)

    val rpb by tag("registerPredicates body", SampleData.Body)
    val rps by tag("registerPredicates signature", SampleData.Signature)

    val nesta by tag("getNestedClassifiersNames all")
    val nests by tag("getNestedClassifiersNames signature", SampleData.Signature)
    val nestp by tag("getNestedClassifiersNames param", SampleData.Parameters)
    val nestb by tag("getNestedClassifiersNames body", SampleData.Body)

    val classa by tag("generateNestedClassLikeDeclaration all")
    val classs by tag("generateNestedClassLikeDeclaration signature", SampleData.Signature)
    val classp by tag("generateNestedClassLikeDeclaration param", SampleData.Parameters)
    val classb by tag("generateNestedClassLikeDeclaration body", SampleData.Body)
    val class1 by tag("generateNestedClassLikeDeclaration focus 1")
    val class2 by tag("generateNestedClassLikeDeclaration focus 2")
    val class3 by tag("generateNestedClassLikeDeclaration focus 3")

    val calls by tag("getCallableNamesForClass signature", SampleData.Signature)
    val callp by tag("getCallableNamesForClass param", SampleData.Parameters)
    val callb by tag("getCallableNamesForClass body", SampleData.Body)

    val ctors by tag("generateConstructors signature", SampleData.Signature)
    val ctorp by tag("generateConstructors param", SampleData.Parameters)
    val ctorb by tag("generateConstructors body", SampleData.Body)

    val props by tag("generateProperties signature", SampleData.Signature)
    val propp by tag("generateProperties param", SampleData.Parameters)
    val propb by tag("generateProperties body", SampleData.Body)

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

            private val BUILDABLE_PREDICATE = DeclarationPredicate.create {
              annotated(FqName("dev.bnorm.buildable.Buildable"))
            }

            private val HAS_BUILDABLE_PREDICATE = DeclarationPredicate.create {
              hasAnnotated(FqName("dev.bnorm.buildable.Buildable"))
            }
          ${cob}}

          ${rps}override fun FirDeclarationPredicateRegistrar.${funName}registerPredicates${funName}()${rps} {${rpb}
            register(BUILDABLE_PREDICATE)
            register(HAS_BUILDABLE_PREDICATE)
          ${rpb}}

          ${nesta}${nests}override fun ${funName}getNestedClassifiersNames${funName}(${nestp}
            classSymbol: FirClassSymbol<*>,
            context: NestedClassGenerationContext,
          ${nestp}): Set<Name>${nests} {${nestb}
            val provider = session.predicateBasedProvider
            if (!provider.matches(HAS_BUILDABLE_PREDICATE, classSymbol))
              return emptySet()

            return setOf(BUILDER_CLASS_NAME)
          ${nestb}}${nesta}

          ${classa}${classs}override fun ${funName}generateNestedClassLikeDeclaration${funName}(${classp}
            owner: FirClassSymbol<*>,
            name: Name,
            context: NestedClassGenerationContext,
          ${classp}): FirClassLikeSymbol<*>?${classs} {${classb}${class1}
            if (name != BUILDER_CLASS_NAME) return null${class1}

            ${class2}val scope: FirScope =
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
          ${classb}}${classa}

          ${calls}override fun ${funName}getCallableNamesForClass${funName}(${callp}
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

          ${ctors}override fun ${funName}generateConstructors${funName}(${ctorp}
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

          ${props}override fun ${funName}generateProperties${funName}(${propp}
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

          ${funs}override fun ${funName}generateFunctions${funName}(${funp}
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
    """.trimIndent().toCodeSample()

    val start = base.collapse(SampleData.Body)
        .collapse(SampleData.Parameters)

    base // Start with base for validation
        .then { start.focus(clsSig) }

        .then { focus(funName, scroll = false) }

        // companion object
        .then { reveal(cob).focus(cob, scroll = false).attach(BOOK_PANEL) }
        .then { attach(BOOK_PANEL.show()) }
        .then { attach(BOOK_PANEL) }

        // registerPredicates
        .then { start.focus(rps, scroll = false) }
        .then { reveal(rpb).focus(rpb, scroll = false) }

        // getNestedClassifiersNames
        .then { start.reveal(nestp).focus(nests) }
        .then { reveal(nestb).focus(nestb).scroll(nests).attach(BUILDER_CLASS_NAME_PANEL) }
        .then { focus(nesta).attach(BUILDER_CLASS_NAME_PANEL.show()) }
        .then { attach(BUILDER_CLASS_NAME_PANEL.showNext()) }
        .then { attach(BUILDER_CLASS_NAME_PANEL.next()) }
        .instead { start.focus(nests).attach(data) }

        // generateNestedClassLikeDeclaration
        .then { start.reveal(classp).focus(classs) }
        .then { reveal(classb).focus(class1).scroll(classs) }
        .then { focus(class2) }
        .then { focus(class3).attach(BUILDER_CLASS_KEY_PANEL) }
        .then { attach(BUILDER_CLASS_KEY_PANEL.show()) }
        .then { attach(BUILDER_CLASS_KEY_PANEL) }
        .then { focus(classa).attach(BUILDER_CLASS_SYMBOL_PANEL) }
        .then { attach(BUILDER_CLASS_SYMBOL_PANEL.show()) }
        .then { attach(BUILDER_CLASS_SYMBOL_PANEL.showNext()) }
        .then { attach(BUILDER_CLASS_SYMBOL_PANEL.next()) }
        .instead { start.focus(classs).attach(data) }

        // getCallableNamesForClass
        .then { start.reveal(callp).focus(calls) }
        .then { reveal(callb).focus(callb).scroll(calls).attach(BUILDER_CALL_NAME_PANEL) }
        .then { attach(BUILDER_CALL_NAME_PANEL.show()) }
        .then { attach(BUILDER_CALL_NAME_PANEL.showNext()) }
        .then { attach(BUILDER_CALL_NAME_PANEL.next()) }
        .instead { start.focus(calls).attach(data) }

        // generateConstructors
        .then { start.reveal(ctorp).focus(ctors) }
        .then { reveal(ctorb).focus(ctorb).scroll(ctors).attach(BUILDER_CONSTRUCTOR_PANEL) }
        .then { attach(BUILDER_CONSTRUCTOR_PANEL.show()) }
        .then { attach(BUILDER_CONSTRUCTOR_PANEL.showNext()) }
        .then { attach(BUILDER_CONSTRUCTOR_PANEL.next()) }
        .instead { start.focus(ctors).attach(data) }

        // generateProperties
        .then { start.reveal(propp).focus(props) }
        .then { reveal(propb).focus(propb).scroll(props).attach(BUILDER_PROPERTY_PANEL) }
        .then { attach(BUILDER_PROPERTY_PANEL.show()) }
        .then { attach(BUILDER_PROPERTY_PANEL.showNext()) }
        .then { attach(BUILDER_PROPERTY_PANEL.next()) }
        .instead { start.focus(props).attach(data) }

        // generateFunctions
        .then { start.reveal(funp).focus(funs) }
        .then { reveal(funb).focus(funb).scroll(funs).attach(BUILDER_FUNCTION_PANEL) }
        .then { attach(BUILDER_FUNCTION_PANEL.show()) }
        .then { attach(BUILDER_FUNCTION_PANEL.showNext()) }
        .then { attach(BUILDER_FUNCTION_PANEL.next()) }
        .instead { start.focus(funs).attach(data) }

        .then { start }
}

@Composable
internal fun validateFirGenerationSample() {
    validateSample(
        sample = VALIDATE_SAMPLES[0].string,
        file = "buildable/compiler-plugin/dev/bnorm/buildable/plugin/fir/BuildableFirDeclarationGenerationExtension.kt@BuildableFirDeclarationGenerationExtension"
    )
}

private val SAMPLES = VALIDATE_SAMPLES.subList(fromIndex = 1, toIndex = VALIDATE_SAMPLES.size)

fun StoryboardBuilder.FirGeneration(sink: MutableList<CodeSample>, start: Int = 0, endExclusive: Int = SAMPLES.size) {
    sink.addAll(SAMPLES)
    StageSampleScene(SAMPLES, CompilerStage.Resolve, start, endExclusive)
}
