package dev.bnorm.evolved.sections.today

import dev.bnorm.storyboard.core.StoryboardBuilder

fun StoryboardBuilder.ComplexTransformation() {
    CodeTransitionSlide(TRUNCATED_COMPLEX_TRANSITIONS)
}

private val COMPLEX_TRANSITIONS = listOf(
    """
        assert(<m>str</m=1>.length >= 1 && str[0] == 'x')
    """.trimIndent() to """
        <i>val tmp1 = </i><m>str</m=1>
        assert(<i>tmp1</i>.length >= 1 && str[0] == 'x')
    """.trimIndent(),

    """
        val tmp1 = str
        assert(<m>tmp1.length</m=2> >= 1 && str[0] == 'x')
    """.trimIndent() to """
        val tmp1 = str
        <i>val tmp2 = </i><m>tmp1.length</m=2>
        assert(<i>tmp2</i> >= 1 && str[0] == 'x')
    """.trimIndent(),

    """
        val tmp1 = str
        val tmp2 = tmp1.length
        assert(<m>tmp2 >= 1</m=3> && str[0] == 'x')
    """.trimIndent() to """
        val tmp1 = str
        val tmp2 = tmp1.length
        <i>val tmp3 = </i><m>tmp2 >= 1</m=3>
        assert(<i>tmp3</i> && str[0] == 'x')
    """.trimIndent(),

    """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        assert(<m>tmp3</m=4><i> && </i>str[0] == 'x')
    """.trimIndent() to """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        <i>if (</i><m>tmp3</m=4><i>) {</i>
        <i>    </i>assert(<i></i>str[0] == 'x')
        <i>} else {</i>
        <i>    assert(false)</i>
        <i>}</i>
    """.trimIndent(),

    """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        if (tmp3) {
            assert(<m>str</m=5>[0] == 'x')
        } else {
            assert(false)
        }
    """.trimIndent() to """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        if (tmp3) {
        <i>    val tmp4 = </i><m>str</m=5>
            assert(<i>tmp4</i>[0] == 'x')
        } else {
            assert(false)
        }
    """.trimIndent(),

    """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        if (tmp3) {
            val tmp4 = str
            assert(<m>tmp4[0]</m=6> == 'x')
        } else {
            assert(false)
        }
    """.trimIndent() to """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        if (tmp3) {
            val tmp4 = str
        <i>    val tmp5 = </i><m>tmp4[0]</m=6>
            assert(<i>tmp5</i> == 'x')
        } else {
            assert(false)
        }
    """.trimIndent(),

    """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        if (tmp3) {
            val tmp4 = str
            val tmp5 = tmp4[0]
            assert(<m>tmp5 == 'x'</m=7>)
        } else {
            assert(false)
        }
    """.trimIndent() to """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        if (tmp3) {
            val tmp4 = str
            val tmp5 = tmp4[0]
        <i>    val tmp6 = </i><m>tmp5 == 'x'</m=7>
            assert(<i>tmp6</i>)
        } else {
            assert(false)
        }
    """.trimIndent(),

    """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        if (tmp3) {
            val tmp4 = str
            val tmp5 = tmp4[0]
            val tmp6 = tmp5 == 'x'
            assert(tmp6)
        } else {
            assert(false)
        }
    """.trimIndent() to """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        if (tmp3) {
            val tmp4 = str
            val tmp5 = tmp4[0]
            val tmp6 = tmp5 == 'x'
            assert(tmp6)
        } else {
            assert(false)<i> {</i>
        <i>        "${'"'}"</i>
        <i>            assert(str.length >= 1 && str[0] == 'x')</i>
        <i>                   |   |      |</i>
        <i>                   |   |      ${'$'}tmp3</i>
        <i>                   |   ${'$'}tmp2</i>
        <i>                   ${'$'}tmp1</i>
        <i>        "${'"'}".trimIndent()</i>
        <i>    }</i>
        }
    """.trimIndent(),

    """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        if (tmp3) {
            val tmp4 = str
            val tmp5 = tmp4[0]
            val tmp6 = tmp5 == 'x'
            assert(tmp6)
        } else {
            assert(false) {
                "${'"'}"
                    assert(str.length >= 1 && str[0] == 'x')
                           |   |      |
                           |   |      ${'$'}tmp3
                           |   ${'$'}tmp2
                           ${'$'}tmp1
                "${'"'}".trimIndent()
            }
        }
    """.trimIndent() to """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        if (tmp3) {
            val tmp4 = str
            val tmp5 = tmp4[0]
            val tmp6 = tmp5 == 'x'
            assert(tmp6)<i> {</i>
        <i>        "${'"'}"</i>
        <i>            assert(str.length >= 1 && str[0] == 'x')</i>
        <i>                   |   |      |       |  |   |</i>
        <i>                   |   |      |       |  |   ${'$'}tmp6</i>
        <i>                   |   |      |       |  ${'$'}tmp5</i>
        <i>                   |   |      |       ${'$'}tmp4</i>
        <i>                   |   |      ${'$'}tmp3</i>
        <i>                   |   ${'$'}tmp2</i>
        <i>                   ${'$'}tmp1</i>
        <i>        "${'"'}".trimIndent()</i>
        <i>    }</i>
        } else {
            assert(false) {
                "${'"'}"
                    assert(str.length >= 1 && str[0] == 'x')
                           |   |      |
                           |   |      ${'$'}tmp3
                           |   ${'$'}tmp2
                           ${'$'}tmp1
                "${'"'}".trimIndent()
            }
        }
    """.trimIndent(),

    """
        <i>val tmp1 = </i><m>str</m=1>
        <i>val tmp2 = tmp1</i><m>.length</m=2>
        <i>val tmp3 = tmp2</i><m> >= 1</m=3>
        <i>if (tmp3) {</i>
        <i>    val tmp4 = </i><m>str</m=4>
        <i>    val tmp5 = tmp4</i><m>[0]</m=5>
        <i>    val tmp6 = tmp5</i><m> == 'x'</m=6>
        <i>    </i><m>assert(</m=0>tmp6</i><m>)</m=7> {</i>
        <i>        "${'"'}"</i>
        <i>            assert(str.length >= 1 && str[0] == 'x')</i>
        <i>                   |   |      |       |  |   |</i>
        <i>                   |   |      |       |  |   ${'$'}tmp6</i>
        <i>                   |   |      |       |  ${'$'}tmp5</i>
        <i>                   |   |      |       ${'$'}tmp4</i>
        <i>                   |   |      ${'$'}tmp3</i>
        <i>                   |   ${'$'}tmp2</i>
        <i>                   ${'$'}tmp1</i>
        <i>        "${'"'}".trimIndent()</i>
        <i>    }</i>
        <i>} else {</i>
        <i>    assert(false) {</i>
        <i>        "${'"'}"</i>
        <i>            assert(str.length >= 1 && str[0] == 'x')</i>
        <i>                   |   |      |</i>
        <i>                   |   |      ${'$'}tmp3</i>
        <i>                   |   ${'$'}tmp2</i>
        <i>                   ${'$'}tmp1</i>
        <i>        "${'"'}".trimIndent()</i>
        <i>    }</i>
        <i>}</i>
    """.trimIndent() to """
        <m>assert(</m=0><m>str</m=1><m>.length</m=2><m> >= 1</m=3><i> && </i><m>str</m=4><m>[0]</m=5><m> == 'x'</m=6><m>)</m=7>
    """.trimIndent(),
)

private val TRUNCATED_COMPLEX_TRANSITIONS = listOf(
    """
        assert(<m>str</m=1>.length >= 1 && str[0] == 'x')
    """.trimIndent() to """
        <i>val tmp1 = </i><m>str</m=1>
        assert(<i>tmp1</i>.length >= 1 && str[0] == 'x')
    """.trimIndent(),

    """
        val tmp1 = str
        assert(<m>tmp1.length</m=2> >= 1 && str[0] == 'x')
    """.trimIndent() to """
        val tmp1 = str
        <i>val tmp2 = </i><m>tmp1.length</m=2>
        assert(<i>tmp2</i> >= 1 && str[0] == 'x')
    """.trimIndent(),

    // TODO tmp3

    """
        <i>val tmp1 = </i><m>str</m=1>
        <i>val tmp2 = tmp1</i><m>.length</m=2>
        assert(<i>tmp2</i> >= 1 && str[0] == 'x')
    """.trimIndent() to """
        assert(<m>str</m=1><m>.length</m=2> >= 1 && str[0] == 'x')
    """.trimIndent(),

    """
        assert(<m>str.length >= 1</m=1><i> && </i><m>str[0] == 'x'</m=2>)
    """.trimIndent() to """
        assert(<i>when {</i>
        <i>    </i><m>str.length >= 1</m=1><i> -> </i><m>str[0] == 'x'</m=2>
        <i>    else -> false</i>
        <i>}</i>)
    """.trimIndent(),
)
