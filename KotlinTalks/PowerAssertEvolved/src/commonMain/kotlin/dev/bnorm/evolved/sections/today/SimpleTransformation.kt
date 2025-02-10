package dev.bnorm.evolved.sections.today

import dev.bnorm.storyboard.core.StoryboardBuilder

fun StoryboardBuilder.SimpleTransformation() {
    // TODO change to be more like the second example

    val transitions = listOf(
        """
            require(<m>employee</m=1>.duration.days < NEWBIE.days)
        """.trimIndent() to """
            <i>val tmp1 = </i><m>employee</m=1>
            require(<i>tmp1</i>.duration.days < NEWBIE.days)
        """.trimIndent(),

        """
            val tmp1 = employee
            require(<m>tmp1.duration</m=2>.days < NEWBIE.days)
        """.trimIndent() to """
            val tmp1 = employee
            <i>val tmp2 = </i><m>tmp1.duration</m=2>
            require(<i>tmp2</i>.days < NEWBIE.days)
        """.trimIndent(),

        """
            val tmp1 = employee
            val tmp2 = tmp1.duration
            require(<m>tmp2.days</m=3> < NEWBIE.days)
        """.trimIndent() to """
            val tmp1 = employee
            val tmp2 = tmp1.duration
            <i>val tmp3 = </i><m>tmp2.days</m=3>
            require(<i>tmp3</i> < NEWBIE.days)
        """.trimIndent(),

        """
            val tmp1 = employee
            val tmp2 = tmp1.duration
            val tmp3 = tmp2.days
            require(tmp3 < <m>NEWBIE</m=4>.days)
        """.trimIndent() to """
            val tmp1 = employee
            val tmp2 = tmp1.duration
            val tmp3 = tmp2.days
            <i>val tmp4 = </i><m>NEWBIE</m=4>
            require(tmp3 < <i>tmp4</i>.days)
        """.trimIndent(),

        """
            val tmp1 = employee
            val tmp2 = tmp1.duration
            val tmp3 = tmp2.days
            val tmp4 = NEWBIE
            require(tmp3 < <m>tmp4.days</m=5>)
        """.trimIndent() to """
            val tmp1 = employee
            val tmp2 = tmp1.duration
            val tmp3 = tmp2.days
            val tmp4 = NEWBIE
            <i>val tmp5 = </i><m>tmp4.days</m=5>
            require(tmp3 < <i>tmp5</i>)
        """.trimIndent(),

        """
            val tmp1 = employee
            val tmp2 = tmp1.duration
            val tmp3 = tmp2.days
            val tmp4 = NEWBIE
            val tmp5 = tmp4.days
            require(<m>tmp3 < tmp5</m=6>)
        """.trimIndent() to """
            val tmp1 = employee
            val tmp2 = tmp1.duration
            val tmp3 = tmp2.days
            val tmp4 = NEWBIE
            val tmp5 = tmp4.days
            <i>val tmp6 = </i><m>tmp3 < tmp5</m=6>
            require(<i>tmp6</i>)
        """.trimIndent(),

        """
            val tmp1 = employee
            val tmp2 = tmp1.duration
            val tmp3 = tmp2.days
            val tmp4 = NEWBIE
            val tmp5 = tmp4.days
            val tmp6 = tmp3 < tmp5
            require(tmp6)
        """.trimIndent() to """
            val tmp1 = employee
            val tmp2 = tmp1.duration
            val tmp3 = tmp2.days
            val tmp4 = NEWBIE
            val tmp5 = tmp4.days
            val tmp6 = tmp3 < tmp5
            require(tmp6) <i>{</i>
            <i>    "${'"'}"</i>
            <i>        require(employee.duration.days < NEWBIE.days)</i>
            <i>                |        |        |    | |      |</i>
            <i>                |        |        |    | |      ${"$"}tmp5</i>
            <i>                |        |        |    | ${"$"}tmp4</i>
            <i>                |        |        |    ${"$"}tmp6</i>
            <i>                |        |        ${"$"}tmp3</i>
            <i>                |        ${"$"}tmp2</i>
            <i>                ${"$"}tmp1</i>
            <i>    "${'"'}".trimIndent()</i>
            <i>}</i>
        """.trimIndent(),
    )

    CodeTransitionSlide(transitions)
}