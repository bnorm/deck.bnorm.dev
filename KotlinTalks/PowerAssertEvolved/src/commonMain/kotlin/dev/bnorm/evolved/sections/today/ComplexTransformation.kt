package dev.bnorm.evolved.sections.today

import dev.bnorm.storyboard.core.StoryboardBuilder

fun StoryboardBuilder.ComplexTransformation() {
    CodeTransitionSlide(COMPLEX_TRANSITIONS)
}

private val COMPLEX_TRANSITIONS = listOf(
    """
        require(<m>str</m=1>.length >= 1 && str[0] == 'x')
    """.trimIndent() to """
        <i>val tmp1 = </i><m>str</m=1>
        require(<i>tmp1</i>.length >= 1 && str[0] == 'x')
    """.trimIndent(),

    """
        val tmp1 = str
        require(<m>tmp1.length</m=2> >= 1 && str[0] == 'x')
    """.trimIndent() to """
        val tmp1 = str
        <i>val tmp2 = </i><m>tmp1.length</m=2>
        require(<i>tmp2</i> >= 1 && str[0] == 'x')
    """.trimIndent(),

    """
        val tmp1 = str
        val tmp2 = tmp1.length
        require(<m>tmp2 >= 1</m=3> && str[0] == 'x')
    """.trimIndent() to """
        val tmp1 = str
        val tmp2 = tmp1.length
        <i>val tmp3 = </i><m>tmp2 >= 1</m=3>
        require(<i>tmp3</i> && str[0] == 'x')
    """.trimIndent(),

    """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        require(<m>tmp3</m=4><i> && </i>str[0] == 'x')
    """.trimIndent() to """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        <i>if (</i><m>tmp3</m=4><i>) {</i>
        <i>    </i>require(<i></i>str[0] == 'x')
        <i>} else {</i>
        <i>    require(false)</i>
        <i>}</i>
    """.trimIndent(),

    """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        if (tmp3) {
            require(<m>str</m=5>[0] == 'x')
        } else {
            require(false)
        }
    """.trimIndent() to """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        if (tmp3) {
        <i>    val tmp4 = </i><m>str</m=5>
            require(<i>tmp4</i>[0] == 'x')
        } else {
            require(false)
        }
    """.trimIndent(),

    """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        if (tmp3) {
            val tmp4 = str
            require(<m>tmp4[0]</m=6> == 'x')
        } else {
            require(false)
        }
    """.trimIndent() to """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        if (tmp3) {
            val tmp4 = str
        <i>    val tmp5 = </i><m>tmp4[0]</m=6>
            require(<i>tmp5</i> == 'x')
        } else {
            require(false)
        }
    """.trimIndent(),

    """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        if (tmp3) {
            val tmp4 = str
            val tmp5 = tmp4[0]
            require(<m>tmp5 == 'x'</m=7>)
        } else {
            require(false)
        }
    """.trimIndent() to """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        if (tmp3) {
            val tmp4 = str
            val tmp5 = tmp4[0]
        <i>    val tmp6 = </i><m>tmp5 == 'x'</m=7>
            require(<i>tmp6</i>)
        } else {
            require(false)
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
            require(tmp6)
        } else {
            require(false)
        }
    """.trimIndent() to """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        if (tmp3) {
            val tmp4 = str
            val tmp5 = tmp4[0]
            val tmp6 = tmp5 == 'x'
            require(tmp6)
        } else {
            require(false)<i> {</i>
        <i>        "${'"'}"</i>
        <i>            require(str.length >= 1 && str[0] == 'x')</i>
        <i>                    |   |      |</i>
        <i>                    |   |      ${'$'}tmp3</i>
        <i>                    |   ${'$'}tmp2</i>
        <i>                    ${'$'}tmp1</i>
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
            require(tmp6)
        } else {
            require(false) {
                "${'"'}"
                    require(str.length >= 1 && str[0] == 'x')
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
            require(tmp6)<i> {</i>
        <i>        "${'"'}"</i>
        <i>            require(str.length >= 1 && str[0] == 'x')</i>
        <i>                    |   |      |       |  |   |</i>
        <i>                    |   |      |       |  |   ${'$'}tmp6</i>
        <i>                    |   |      |       |  ${'$'}tmp5</i>
        <i>                    |   |      |       ${'$'}tmp4</i>
        <i>                    |   |      ${'$'}tmp3</i>
        <i>                    |   ${'$'}tmp2</i>
        <i>                    ${'$'}tmp1</i>
        <i>        "${'"'}".trimIndent()</i>
        <i>    }</i>
        } else {
            require(false) {
                "${'"'}"
                    require(str.length >= 1 && str[0] == 'x')
                            |   |      |
                            |   |      ${'$'}tmp3
                            |   ${'$'}tmp2
                            ${'$'}tmp1
                "${'"'}".trimIndent()
            }
        }
    """.trimIndent(),
)