package dev.bnorm.evolved.sections.today

import dev.bnorm.storyboard.StoryboardBuilder

fun StoryboardBuilder.ExampleTransformation() {
    CodeTransitionSlide(EXAMPLE_TRANSITIONS)
}

private val EXAMPLE_TRANSITIONS = listOf(
    """
        assert(<m>"Hello".length</m=1> == "World".substring(1, 4).length)
    """.trimIndent() to """
        <i>val tmp1 = </i><m>"Hello".length</m=1>
        assert(<i>tmp1</i> == "World".substring(1, 4).length)
    """.trimIndent(),

    """
        val tmp1 = "Hello".length
        assert(tmp1 == <m>"World".substring(1, 4)</m=2>.length)
    """.trimIndent() to """
        val tmp1 = "Hello".length
        <i>val tmp2 = </i><m>"World".substring(1, 4)</m=2>
        assert(tmp1 == <i>tmp2</i>.length)
    """.trimIndent(),

    """
        val tmp1 = "Hello".length
        val tmp2 = "World".substring(1, 4)
        assert(tmp1 == <m>tmp2.length</m=3>)
    """.trimIndent() to """
        val tmp1 = "Hello".length
        val tmp2 = "World".substring(1, 4)
        <i>val tmp3 = </i><m>tmp2.length</m=3>
        assert(tmp1 == <i>tmp3</i>)
    """.trimIndent(),

    """
        val tmp1 = "Hello".length
        val tmp2 = "World".substring(1, 4)
        val tmp3 = tmp2.length
        assert(<m>tmp1 == tmp3</m=4>)
    """.trimIndent() to """
        val tmp1 = "Hello".length
        val tmp2 = "World".substring(1, 4)
        val tmp3 = tmp2.length
        <i>val tmp4 = </i><m>tmp1 == tmp3</m=4>
        assert(<i>tmp4</i>)
    """.trimIndent(),

    """
        val tmp1 = "Hello".length
        val tmp2 = "World".substring(1, 4)
        val tmp3 = tmp2.length
        val tmp4 = tmp1 == tmp3
        assert(tmp4)
    """.trimIndent() to """
        val tmp1 = "Hello".length
        val tmp2 = "World".substring(1, 4)
        val tmp3 = tmp2.length
        val tmp4 = tmp1 == tmp3
        assert(tmp4)<i> {</i>
        <i>    "${'"'}"</i>
        <i>        assert("Hello".length == "World".substring(1, 4).length</i>
        <i>                       |      |          |               |</i>
        <i>                       |      |          |               ${"$"}tmp3</i>
        <i>                       |      |          ${"$"}tmp2</i>
        <i>                       |      ${"$"}tmp4</i>
        <i>                       ${"$"}tmp1</i>
        <i>    "${'"'}".trimIndent()</i>
        <i>}</i>
    """.trimIndent(),
)
