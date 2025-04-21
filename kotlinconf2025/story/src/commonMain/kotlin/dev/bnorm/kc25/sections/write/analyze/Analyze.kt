package dev.bnorm.kc25.sections.write.analyze

import dev.bnorm.storyboard.StoryboardBuilder

fun StoryboardBuilder.Analyze() {
    // TODO create a checker for no type parameters as another example?
    //  - or maybe leave this to the companion?

    // TODO what order should these be?
    CheckerExtension()
    Checker()
    Errors()
}
