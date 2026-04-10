package dev.bnorm.kc26

import dev.bnorm.kc26.components.TimelineScene
import dev.bnorm.kc26.components.TimelineState
import dev.bnorm.kc26.sections.Closing
import dev.bnorm.kc26.sections.FutureSection
import dev.bnorm.kc26.sections.MajorSection
import dev.bnorm.kc26.sections.MinorSection
import dev.bnorm.kc26.sections.StartSection
import dev.bnorm.kc26.sections.Title
import dev.bnorm.kc26.template.gradientDecorator
import dev.bnorm.kc26.template.themeDecorator
import dev.bnorm.storyboard.ContentDecorator
import dev.bnorm.storyboard.Storyboard

fun createStoryboard(
    decorator: ContentDecorator = ContentDecorator.None,
): Storyboard = Storyboard.build(
    title = "Powering Up Your Assertions",
    description = """
        Big changes to Power-Assert are headed your way with Kotlin 2.4! We’ve heard your complaints about complex
        Gradle configuration, confusing message diagrams, and poor library integration. We’re hoping to address all of
        these areas, and I’ll show you how!

        But the good news doesn’t stop there. If you’re an assertion library author, you may be interested in how you
        can add first-party support for Power-Assert to your library. We want every assertion library to have the same
        great, out-of-the-box experience!

        And we might even take a brief look at what we’ll be working on next!
    """.trimIndent(),
    decorator = ContentDecorator.from(
        themeDecorator(),
        decorator,
        gradientDecorator(),
    ),
) {
    // TODO go through samples and pick a fun theme
    //  - LOTR again?
    //  - star trek?
    //  - make it a choice from the audience companion?!
    //  - examples should include...
    //      - an array to show improvement there
    //      - a String to show quotes

    // TODO Companion
    //  - survey of who knows/uses power-assert
    //  - scene reactions?

    // TODO Pre-slide: survey of who knows/uses power-assert
    //  - use companion to survey audience about their experience with power-assert

    Title()

    // TODO do we need the third-party dot?
    TimelineScene(end = TimelineState.Bundled)
    // TODO i'm not happy with the transition between timeline and sections
    //  - i like the expand of the timeline (maybe it's a zoom instead?)
    //  - but the entrance of the next scene is weird coming out of the gradient
    //  - there needs to be a reason to expand into the gradient like that
    //    - maybe double expand a gradient and then a white box?
    StartSection()
    TimelineScene(start = TimelineState.Bundled, end = TimelineState.Improvements)
    MinorSection()
    TimelineScene(start = TimelineState.Improvements, end = TimelineState.Explanations)
    MajorSection()
    TimelineScene(start = TimelineState.Explanations, end = TimelineState.Future)
    FutureSection()

//    sectioned {
//        WhatIsIt()
//
//        WhatIsChanging()
//        // TODO merge changing and new?
//        WhatIsNew()
//
//        WhatIsPossible()
//
//        // TODO do we need a summary at this point? to review problems and goals?
//        // TODO QRCode to example GitHub repository
//
//        WhatIsNext()
//    }

    // TODO Closing (waiting on template)
    Closing()
}
