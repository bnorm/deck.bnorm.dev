package dev.bnorm.kc25.sections.write.transform

import androidx.compose.runtime.Composable
import dev.bnorm.kc25.components.validateSample
import dev.bnorm.kc25.sections.stages.CompilerStage
import dev.bnorm.kc25.template.INTELLIJ_DARK_CODE_STYLE
import dev.bnorm.kc25.template.StageSampleScene
import dev.bnorm.kc25.template.code.buildCodeSamples
import dev.bnorm.storyboard.StoryboardBuilder

private sealed class SampleData {
    data object Body : SampleData()
    data object Visitor : SampleData()
    data object Helper : SampleData()
}

private val VALIDATE_SAMPLES = buildCodeSamples {
    // TODO include UnsafeDuringIrConstructionAPI opt-in annotation?
    // TODO generateBuildFunction references the primary constructor instead of the annotated constructor
    // TODO add side panels for generated code

    val sup by tag("super class")
    val co by tag("companion object")
    val sig by tag("signatures")
    val uninit by tag("") // TODO

    val vEle by tag("visitElement", SampleData.Visitor)
    val vEleB by tag("visitElement body", SampleData.Body)

    val aCtor by tag("", SampleData.Visitor)
    val vCtorB by tag("visitConstructor body", SampleData.Body)

    val gCtor by tag("generateConstructor", SampleData.Helper)
    val gCtorS by tag("generateConstructor signature")
    val gCtorB by tag("generateConstructor body", SampleData.Body)

    val aCls by tag("", SampleData.Visitor)
    val vClsB by tag("visitClass body", SampleData.Body)
    // TODO focus on parts of function body
    // TODO side panel for irAttribute

    val gBack by tag("generateBacking", SampleData.Helper)
    val gBackS by tag("generateBacking signature")
    val gBackB by tag("generateBacking body", SampleData.Body)
    // TODO focus on parts of function body

    val uProp by tag("updatePropertyAccessors", SampleData.Helper)
    val uPropS by tag("updatePropertyAccessors signature")
    val uPropB by tag("updatePropertyAccessors body", SampleData.Body)
    // TODO focus on parts of function body

    val aFun by tag("", SampleData.Visitor)
    val vFunB by tag("visitSimpleFunction body", SampleData.Body)

    val gFun by tag("generateBuildFunction", SampleData.Helper)
    val gFunS by tag("generateBuildFunction signature")
    val gFunB by tag("generateBuildFunction body", SampleData.Body)
    // TODO focus on parts of function body

    val gArg by tag("generateConstructorArguments", SampleData.Helper)
    val gArgS by tag("generateConstructorArguments signature")
    val gArgB by tag("generateConstructorArguments body", SampleData.Body)
    // TODO focus on parts of function body

    val base = """
        class BuildableIrVisitor(
          private val context: IrPluginContext,
        ) : ${sup}IrVisitorVoid()${sup} {
          companion object {${co}
            private val BUILDABLE_ORIGIN = GeneratedByPlugin(BuildableKey)

            private val ILLEGAL_STATE_EXCEPTION_FQ_NAME =
              FqName("kotlin.IllegalStateException")
            private val ILLEGAL_STATE_EXCEPTION_CLASS_ID =
              ClassId.topLevel(ILLEGAL_STATE_EXCEPTION_FQ_NAME)
          ${co}}

          ${uninit}private val nullableStringType = context.irBuiltIns.stringType.makeNullable()
          private val illegalStateExceptionConstructor =
            context.referenceConstructors(ILLEGAL_STATE_EXCEPTION_CLASS_ID)
              .single { constructor ->
                val parameter = constructor.owner.valueParameters.singleOrNull()
                  ?: return@single false
                parameter.type == nullableStringType
              }

          private fun IrBuilderWithScope.irUninitializedProperty(
            property: IrDeclarationWithName,
          ): IrConstructorCall {
            return irCall(illegalStateExceptionConstructor).apply {
              putValueArgument(
                0, irString("Uninitialized property '${'$'}{property.name}'.")
              )
            }
          }

          ${uninit}${vEle}${sig}override fun visitElement(element: IrElement)$sig {${vEleB}
            when (element) {
              is IrDeclaration,
              is IrFile,
              is IrModuleFragment,
                -> element.acceptChildrenVoid(this)

              else -> Unit
            }
          ${vEleB}}${vEle}

          ${aCtor}${sig}override fun visitConstructor(declaration: IrConstructor)${sig} {${vCtorB}
            if (declaration.origin == BUILDABLE_ORIGIN && declaration.body == null) {
              declaration.body = generateDefaultConstructor(declaration)
            }
          ${vCtorB}}${gCtor}

          ${gCtorS}private fun generateDefaultConstructor(declaration: IrConstructor): IrBody?${gCtorS} {${gCtorB}
            val parentClass = declaration.parent as? IrClass ?: return null
            val anyConstructor = context.irBuiltIns.anyClass.owner.primaryConstructor
              ?: return null

            val irBuilder = DeclarationIrBuilder(context, declaration.symbol)
            return irBuilder.irBlockBody {
              +irDelegatingConstructorCall(anyConstructor)
              +IrInstanceInitializerCallImpl(
                startOffset, endOffset,
                classSymbol = parentClass.symbol,
                type = context.irBuiltIns.unitType,
              )
            }
          ${gCtorB}}${gCtor}${aCtor}

          ${aCls}${sig}override fun visitClass(declaration: IrClass)$sig {${vClsB}
            val pluginKey = (declaration.origin as? GeneratedByPlugin)?.pluginKey
            if (pluginKey is BuilderClassKey) {
              val declarations = declaration.declarations

              val fields = mutableListOf<IrField>()
              for (property in declarations) {
                if (property !is IrProperty) continue
                val backing = generateBacking(declaration, property)
                updatePropertyAccessors(property, backing)

                property.builderPropertyBacking = backing
                fields += backing.flag
                fields += backing.holder
              }

              declarations.addAll(0, fields)
            }

            declaration.acceptChildrenVoid(this)
          ${vClsB}}${gBack}

          ${gBackS}private fun generateBacking(
            klass: IrClass,
            property: IrProperty,
          ): BuilderPropertyBacking${gBackS} {${gBackB}
            val getter = property.getter!!
            property.backingField = null

            val isPrimitive = getter.returnType.isPrimitiveType()
            val backingType = when {
              isPrimitive -> getter.returnType
              else -> getter.returnType.makeNullable()
            }

            val flagField = context.irFactory.buildField {
              origin = BUILDABLE_ORIGIN
              visibility = DescriptorVisibilities.PRIVATE
              name = Name.identifier("${'$'}{property.name}\${'$'}BuildableFlag")
              type = context.irBuiltIns.booleanType
            }.apply {
              parent = klass
              initializer = context.irFactory.createExpressionBody(
                expression = false.toIrConst(context.irBuiltIns.booleanType)
              )
            }

            val holderField = context.irFactory.buildField {
              origin = BUILDABLE_ORIGIN
              visibility = DescriptorVisibilities.PRIVATE
              name = Name.identifier("${'$'}{property.name}\${'$'}BuildableHolder")
              type = backingType
            }.apply {
              parent = klass
              initializer = context.irFactory.createExpressionBody(
                expression = when (isPrimitive) {
                  true -> IrConstImpl.defaultValueForType(
                    SYNTHETIC_OFFSET,
                    SYNTHETIC_OFFSET,
                    backingType
                  )

                  false -> null.toIrConst(backingType)
                }
              )
            }

            return BuilderPropertyBacking(holderField, flagField)
          ${gBackB}}${gBack}${uProp}

          ${uPropS}private fun updatePropertyAccessors(
            property: IrProperty,
            backing: BuilderPropertyBacking,
          )${uPropS} {${uPropB}
            val getter = property.getter!!
            val setter = property.setter!!
            property.backingField = null

            getter.origin = BUILDABLE_ORIGIN
            getter.body = DeclarationIrBuilder(context, getter.symbol).irBlockBody {
              val dispatch = getter.dispatchReceiverParameter!!
              +irIfThenElse(
                type = getter.returnType,
                condition = irGetField(irGet(dispatch), backing.flag),
                thenPart = irReturn(irGetField(irGet(dispatch), backing.holder)),
                elsePart = irThrow(irUninitializedProperty(property))
              )
            }

            setter.origin = BUILDABLE_ORIGIN
            setter.body = DeclarationIrBuilder(context, setter.symbol).irBlockBody {
              val dispatch = setter.dispatchReceiverParameter!!
              +irSetField(
                receiver = irGet(dispatch),
                field = backing.holder,
                value = irGet(setter.valueParameters[0])
              )
              +irSetField(
                receiver = irGet(dispatch),
                field = backing.flag,
                value = true.toIrConst(context.irBuiltIns.booleanType)
              )
            }
          ${uPropB}}${uProp}${aCls}

          ${aFun}${sig}override fun visitSimpleFunction(declaration: IrSimpleFunction)$sig {${vFunB}
            if (declaration.origin == BUILDABLE_ORIGIN && declaration.body == null) {
              declaration.body = generateBuildFunction(declaration)
            }
          ${vFunB}}${gFun}

          ${gFunS}private fun generateBuildFunction(function: IrSimpleFunction): IrBody?${gFunS} {${gFunB}
            val builderClass = function.parent
              as? IrClass ?: return null
            val irClass = function.returnType.classifierOrNull?.owner
              as? IrClass ?: return null
            val primaryConstructor = irClass.primaryConstructor
              ?: return null

            val irBuilder = DeclarationIrBuilder(context, function.symbol)
            return irBuilder.irBlockBody {
              val arguments = generateConstructorArguments(
                constructorParameters = primaryConstructor.valueParameters,
                builderProperties = builderClass.declarations.filterIsInstance<IrProperty>(),
                dispatchReceiverParameter = function.dispatchReceiverParameter!!
              )

              val constructorCall = irCall(primaryConstructor).apply {
                arguments.forEachIndexed { index, variable ->
                  putValueArgument(index, irGet(variable))
                }
              }

              +irReturn(constructorCall)
            }
          ${gFunB}}${gFun}${gArg}

          ${gArgS}private fun IrBlockBodyBuilder.generateConstructorArguments(
            constructorParameters: List<IrValueParameter>,
            builderProperties: List<IrProperty>,
            dispatchReceiverParameter: IrValueParameter,
          ): List<IrVariable>${gArgS} {${gArgB}
            val variables = mutableListOf<IrVariable>()

            // Transformer to substitute references to previous constructor parameters
            // with references to previous local variables.
            val transformer = object : IrElementTransformerVoid() {
              override fun visitGetValue(expression: IrGetValue): IrExpression {
                val index = constructorParameters
                  .indexOfFirst { it.symbol == expression.symbol }

                return when {
                  index != -1 -> irGet(variables[index])
                  else -> super.visitGetValue(expression)
                }
              }
            }

            for (valueParameter in constructorParameters) {
              val builderPropertyBacking =
                builderProperties.single { it.name == valueParameter.name }
                  .builderPropertyBacking!!

              variables += irTemporary(
                nameHint = valueParameter.name.asString(),
                value = irBlock {
                  val defaultValue = valueParameter.defaultValue
                  val elsePart = when {
                    defaultValue != null ->
                      defaultValue.expression.deepCopyWithoutPatchingParents()
                        .transform(transformer, null)

                    else -> irThrow(irUninitializedProperty(valueParameter))
                  }

                  +irIfThenElse(
                    type = valueParameter.type,
                    condition = irGetField(
                      receiver = irGet(dispatchReceiverParameter),
                      field = builderPropertyBacking.flag
                    ),
                    thenPart = irGetField(
                      receiver = irGet(dispatchReceiverParameter),
                      field = builderPropertyBacking.holder
                    ),
                    elsePart = elsePart
                  )
                },
              )
            }

            return variables
          ${gArgB}}${gArg}${aFun}
        }
    """.trimIndent().toCodeSample(INTELLIJ_DARK_CODE_STYLE)

    val start = base
        .hide(SampleData.Helper)
        .hide(uninit)
        .collapse(SampleData.Body)
        .collapse(co)

    val startPostCtor = start.reveal(gCtor)
    val startPostCls = startPostCtor.reveal(gBack, uProp)
    val startPostFun = startPostCls.reveal(gFun, gArg)

    base
        .then { start }
        .then { focus(sup, scroll = false) }
        .then { focus(sig, scroll = false) }

        .then { reveal(co).focus(co, scroll = false) }
        .then { reveal(uninit).focus(uninit) }

        // Element
        .then { start.focus(vEle) }
        .then { reveal(vEleB).focus(vEleB) }
        .then { start.focus(vEle) }

        // Constructor
        .then { start.focus(aCtor) }
        .then { reveal(vCtorB).focus(vCtorB) }

        .then { reveal(gCtor).focus(gCtor).scroll(gCtorS) }
        .then { reveal(gCtorB).focus(gCtorB) }

        .then { focus(aCtor) }
        .then { startPostCtor.focus(aCtor) }

        // Class
        .then { startPostCtor.focus(aCls) }
        .then { reveal(vClsB).focus(vClsB) }

        .then { reveal(gBack).focus(gBack).scroll(gBackS) }
        .then { reveal(gBackB).focus(gBackB) }

        // TODO focus back on class body to continue there
//        .then { collapse(gBackB).focus(vClsB) }

        .then { reveal(uProp).focus(uProp).scroll(uPropS) }
        .then { reveal(uPropB).focus(uPropB) }

        .then { focus(aCls) }
        .then { startPostCls.focus(aCls) }

        // Function
        .then { startPostCls.focus(aFun) }
        .then { reveal(vFunB).focus(vFunB) }

        .then { reveal(gFun).focus(gFun).scroll(gFunS) }
        .then { reveal(gFunB).focus(gFunB) }

        .then { reveal(gArg).focus(gArg).scroll(gArgS) }
        .then { reveal(gArgB).focus(gArgB) }

        // TODO focus back on gFunB

        .then { focus(aFun) }
        .then { startPostFun.focus(aFun) }

        // Final
        .then { startPostFun.unfocus(unscroll = true) }
}

@Composable
internal fun validateVisitorSample() {
    validateSample(
        sample = VALIDATE_SAMPLES[0].string,
        file = "buildable/compiler-plugin/dev/bnorm/buildable/plugin/ir/BuildableIrVisitor.kt@BuildableIrVisitor"
    )
}

private val SAMPLES = VALIDATE_SAMPLES.subList(fromIndex = 1, toIndex = VALIDATE_SAMPLES.size)

fun StoryboardBuilder.Visitor() {
    StageSampleScene(SAMPLES, CompilerStage.Transform, 0, SAMPLES.size)
}
