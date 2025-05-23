package dev.bnorm.kc25.sections.write.transform

import androidx.compose.runtime.Composable
import dev.bnorm.kc25.components.validateSample
import dev.bnorm.kc25.sections.stages.CompilerStage
import dev.bnorm.kc25.template.StageSampleScene
import dev.bnorm.kc25.template.code.CodeSample
import dev.bnorm.kc25.template.code.buildCodeSamples
import dev.bnorm.storyboard.StoryboardBuilder

private sealed class SampleData {
    data object Body : SampleData()
    data object Visitor : SampleData()
    data object Helper : SampleData()
}

private val VALIDATE_SAMPLES = buildCodeSamples {
    val sup by tag("super class")
    val co by tag("companion object")
    val sig by tag("signatures")
    val uninit by tag("") // TODO
    val hide by tag("") // TODO
    val collapse by tag("") // TODO

    val vEle by tag("visitElement", SampleData.Visitor)
    val vEleB by tag("visitElement body", SampleData.Body)

    val vCtor by tag("", SampleData.Visitor)
    val vCtorB by tag("visitConstructor body", SampleData.Body)

    val vCls by tag("", SampleData.Visitor)
    val vClsB by tag("visitClass body", SampleData.Body)
    val vClsB1 by tag("visitClass body 1")
    val vClsB2 by tag("visitClass body 2")
    val vClsB3 by tag("visitClass body 3")
    val vClsB4 by tag("visitClass body 4")

    val vFun by tag("", SampleData.Visitor)
    val vFunB by tag("visitSimpleFunction body", SampleData.Body)

    val base = """
        class BuildableIrVisitor(
          private val context: IrPluginContext,
        ) : ${sup}IrVisitorVoid()${sup} {
          companion object {${co}
            private val BUILDABLE_ORIGIN = GeneratedByPlugin(BuildableKey)${hide}

            private val ILLEGAL_STATE_EXCEPTION_FQ_NAME =
              FqName("kotlin.IllegalStateException")
            private val ILLEGAL_STATE_EXCEPTION_CLASS_ID =
              ClassId.topLevel(ILLEGAL_STATE_EXCEPTION_FQ_NAME)${hide}
          ${co}}${hide}

          private val nullableStringType = context.irBuiltIns.stringType.makeNullable()
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
          }${hide}

          ${vEle}${sig}override fun visitElement(element: IrElement)$sig {${vEleB}
            when (element) {
              is IrDeclaration,
              is IrFile,
              is IrModuleFragment,
                -> element.acceptChildrenVoid(this)

              else -> Unit
            }
          ${vEleB}}${vEle}

          ${vCtor}${sig}override fun visitConstructor(declaration: IrConstructor)${sig} {${vCtorB}
            if (declaration.origin == BUILDABLE_ORIGIN && declaration.body == null) {
              declaration.body =${collapse} generateDefaultConstructor(declaration)${collapse}
            }
          ${vCtorB}}${vCtor}${hide}

          private fun generateDefaultConstructor(declaration: IrConstructor): IrBody? {
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
          }${hide}

          ${vCls}${sig}override fun visitClass(declaration: IrClass)$sig {${vClsB}
            ${vClsB1}val pluginKey = (declaration.origin as? GeneratedByPlugin)?.pluginKey
            if (pluginKey is BuilderClassKey) {${vClsB1}
              val declarations = declaration.declarations

              ${vClsB2}val fields = mutableListOf<IrField>()
              for (property in declarations) {
                if (property !is IrProperty) continue
                val backing =${collapse} generateBacking(declaration, property)${collapse}
                updatePropertyAccessors(property, backing)${hide}

                property.builderPropertyBacking = backing${hide}
                fields += backing.flag
                fields += backing.holder
              }${vClsB2}

              ${vClsB3}declarations.addAll(0, fields)${vClsB3}
            }

            ${vClsB4}declaration.acceptChildrenVoid(this)${vClsB4}
          ${vClsB}}${vCls}${hide}

          private fun generateBacking(
            klass: IrClass,
            property: IrProperty,
          ): BuilderPropertyBacking {
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
          }

          private fun updatePropertyAccessors(
            property: IrProperty,
            backing: BuilderPropertyBacking,
          ) {
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
          }${hide}

          ${vFun}${sig}override fun visitSimpleFunction(declaration: IrSimpleFunction)$sig {${vFunB}
            if (declaration.origin == BUILDABLE_ORIGIN && declaration.body == null) {
              declaration.body =${collapse} generateBuildFunction(declaration)${collapse}
            }
          ${vFunB}}${vFun}${hide}

          private fun generateBuildFunction(function: IrSimpleFunction): IrBody? {
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
          }

          private fun IrBlockBodyBuilder.generateConstructorArguments(
            constructorParameters: List<IrValueParameter>,
            builderProperties: List<IrProperty>,
            dispatchReceiverParameter: IrValueParameter,
          ): List<IrVariable> {
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
          }${hide}
        }
    """.trimIndent().toCodeSample()

    val start = base
        .hide(SampleData.Helper)
        .hide(uninit)
        .collapse(SampleData.Body)
        .collapse(co)
        .hide(hide)
        .collapse(collapse)

    base // Start with base for validation
        .then { start }
        .then { focus(sup, scroll = false) }
        .then { focus(sig, scroll = false) }
        .then { reveal(co).focus(co, scroll = false) }

        // Element
        .then { start.focus(vEle) }
        .then { reveal(vEleB).focus(vEleB) }
        .then { start.focus(vEle) }

        // Constructor
        .then { start.focus(vCtor) }
        .then { reveal(vCtorB).focus(vCtorB) }
        .then { start.focus(vCtor) }

        // Class
        .then { start.focus(vCls) }
        .then { reveal(vClsB).scroll(vClsB).focus(vClsB1, scroll = false) }
        .then { focus(vClsB2, scroll = false) }
        .then { focus(vClsB3, scroll = false) }
        .then { focus(vClsB4, scroll = false) }
        .then { start.focus(vCls) }

        // Function
        .then { start.focus(vFun) }
        .then { reveal(vFunB).focus(vFunB) }
        .then { start.focus(vFun) }

        // Final
        .then { start.unfocus(unscroll = true) }
}

@Composable
internal fun validateVisitorSample() {
    validateSample(
        sample = VALIDATE_SAMPLES[0].string,
        file = "buildable/compiler-plugin/dev/bnorm/buildable/plugin/ir/BuildableIrVisitor.kt@BuildableIrVisitor"
    )
}

private val SAMPLES = VALIDATE_SAMPLES.subList(fromIndex = 1, toIndex = VALIDATE_SAMPLES.size)

fun StoryboardBuilder.Visitor(sink: MutableList<CodeSample>) {
    sink.addAll(SAMPLES)
    StageSampleScene(SAMPLES, CompilerStage.Transform, 0, SAMPLES.size)
}
