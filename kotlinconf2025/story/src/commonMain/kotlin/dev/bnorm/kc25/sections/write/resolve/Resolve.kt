package dev.bnorm.kc25.sections.write.resolve

import dev.bnorm.kc25.template.code.CodeSample
import dev.bnorm.storyboard.StoryboardBuilder

fun StoryboardBuilder.Resolve(sink: MutableList<CodeSample>) {
    // TODO show all the annotation predicate types and what each match?
    // TODO show more details about predicate based provider?
    // TODO show more details about FirScope?
    
    FirGeneration(sink)
}
