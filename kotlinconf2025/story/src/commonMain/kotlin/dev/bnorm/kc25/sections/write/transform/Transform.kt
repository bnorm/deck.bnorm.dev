package dev.bnorm.kc25.sections.write.transform

import dev.bnorm.storyboard.StoryboardBuilder

fun StoryboardBuilder.Transform() {
    // TODO IR section is just way to long
    //  - should we cut it entirely and leave it for people to check out the repository?
    //  - sort of
    //  - cut the details, keep the overview
    //  - still talk about IrAttribute?

    Generation()
    Visitor()
}
