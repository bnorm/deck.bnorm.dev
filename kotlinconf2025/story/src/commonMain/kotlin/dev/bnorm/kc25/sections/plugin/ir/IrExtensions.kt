package dev.bnorm.kc25.sections.plugin.ir

import dev.bnorm.kc25.components.temp.SamplesSlide
import dev.bnorm.kc25.sections.plugin.CompilerPluginRegistrar
import dev.bnorm.kc25.template.SectionAndTitle
import dev.bnorm.storyboard.core.StoryboardBuilder

val IrGenerationExtension = listOf(
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/ir/BuildableIrGenerationExtension.kt@BuildableIrGenerationExtension",
)

val IrVisitorVoid = listOf(
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/ir/BuildableIrVisitor.kt@BuildableIrVisitor",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/ir/BuildableIrVisitor.kt@companion",

    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/ir/BuildableIrVisitor.kt@visitElement",

    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/ir/BuildableIrVisitor.kt@visitConstructor",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/ir/BuildableIrVisitor.kt@generateDefaultConstructor",

    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/ir/BuildableIrVisitor.kt@visitClass",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/ir/BuildableIrVisitor.kt@generateBacking",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/ir/BuildableIrVisitor.kt@irUninitializedProperty",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/ir/BuildableIrVisitor.kt@properties",

    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/ir/BuildableIrVisitor.kt@visitSimpleFunction",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/ir/BuildableIrVisitor.kt@generateBuildFunction",
    "buildable/compiler-plugin/dev/bnorm/buildable/plugin/ir/BuildableIrVisitor.kt@irConstructorArguments",
)

fun StoryboardBuilder.IrExtensions() {
    SectionAndTitle("IR Extensions") {
        SamplesSlide(CompilerPluginRegistrar)

        SamplesSlide(IrGenerationExtension)
        SamplesSlide(IrVisitorVoid)
    }
}
