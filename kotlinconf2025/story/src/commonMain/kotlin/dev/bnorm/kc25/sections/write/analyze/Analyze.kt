package dev.bnorm.kc25.sections.write.analyze

import dev.bnorm.kc25.template.code.CodeSample
import dev.bnorm.storyboard.StoryboardBuilder

fun StoryboardBuilder.Analyze(sink: MutableList<CodeSample>) {
    // TODO create a checker for no type parameters as another example?
    //  - or maybe leave this to the companion?

    // TODO what order should these be?
    CheckerExtension(sink)
    Checker(sink)
    Errors(sink)
}
