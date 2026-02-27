package dev.bnorm.kc26

import dev.bnorm.storyboard.Storyboard

fun createStoryboard(
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
) {
    // TODO Companion
    //  - survey of who knows/uses power-assert
    //  - scene reactions?

    // TODO Pre-slide: survey of who knows/uses power-assert
    //  - use companion to survey audience about their experience with power-assert

    // TODO Title (waiting on template)

    // TODO Section: What's Power-Assert?
    //  - use survey data to adjust introduction?
    //  - quick introduction to what power-assert can do
    //  - quick introduction to how power-assert works
    //  - quick introduction to known problems with using power-assert

    // TODO Section: What's changing for those that use Power-Assert?
    //  - slide showing difference in diagram
    //      - "pause for effect" ... "you're welcome!"
    //      - "wait, that's it?!" ... "you sure promised a lot"
    //  - review how power-assert works
    //  - show change to `CallExplanation(...).toDefaultMessage()`

    // TODO Section: What's new for libraries wanting to use Power-Assert?
    //  - PowerAssert annotation
    //  - function transformation
    //  - CallExplanation and related data structures
    //  - examples of integration
    //      - basic JUnit example
    //      - fluent assertion example

    // TODO Section: What's coming next for Power-Assert?
    //  - the elephant ("not that elephant") : kotlin-test support
    //  - local variables (VariableExplanation and VariableAccessExpression)
    //  - parameter forwarding (ArgumentExplanation and VariableAccessExpression)
    //  - diffs
    //  - other use cases
    //      - pprintln (StringTemplateExpression)
    //      - deep IntelliJ integration (ExplainedException for test failures and Explanations for debugging)
    //  - future ideas
    //      - AST
    //      - cite replacement? (additional source information)
    //      - oh god, lambda tracing...
    //  - generic Explain feature in Kotlin
    //      - balancing features, security, and performance
    //      - syntax idea? calls and variables

    // TODO Closing (waiting on template)
}
