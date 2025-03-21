package dev.bnorm.kc25.components

import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.*
import androidx.compose.ui.unit.dp
import dev.bnorm.kc25.template.CONFERENCE_PURPLE
import dev.bnorm.kc25.template.CONFERENCE_RED
import dev.bnorm.kc25.template.CONFERENCE_YELLOW

@Composable
fun KotlinConfBird(visible: Transition<Boolean>): VectorPainter = rememberVectorPainter(
    name = "KotlinConfBird",
    defaultWidth = 780.dp,
    defaultHeight = 720.dp,
    viewportWidth = 780f,
    viewportHeight = 720f,
    autoMirror = false,
) { _, _ ->
    // TODO the website uses a mask to create the logo highlight sweep
    //  vector painter doesn't seem to support masks? only clips?

    val birdBodyTranslation by visible.animateFloat(
        transitionSpec = {
            when (targetState) {
                true -> tween(durationMillis = 400, delayMillis = 80, easing = EaseOut)
                false -> tween(durationMillis = 200, delayMillis = 900, easing = EaseIn)
            }
        },
        targetValueByState = {
            when (it) {
                false -> 50f
                true -> 0f
            }
        },
    )
    val birdHeadTranslation by visible.animateFloat(
        transitionSpec = {
            when (targetState) {
                true -> tween(durationMillis = 400, delayMillis = 0, easing = EaseOut)
                false -> tween(durationMillis = 200, delayMillis = 940, easing = EaseIn)
            }
        },
        targetValueByState = {
            when (it) {
                false -> 50f
                true -> 0f
            }
        },
    )

    val birdAlpha by visible.animateFloat(
        transitionSpec = {
            when (targetState) {
                true -> tween(durationMillis = 400, delayMillis = 80, easing = EaseOut)
                false -> tween(durationMillis = 200, delayMillis = 900, easing = EaseIn)
            }
        },
        targetValueByState = {
            when (it) {
                false -> 0f
                true -> 1f
            }
        },
    )

    val animatePath by visible.animateFloat(
        transitionSpec = {
            when (targetState) {
                true -> tween(durationMillis = 1000, delayMillis = 400, easing = EaseInCubic)
                false -> tween(durationMillis = 500, delayMillis = 400, easing = EaseOutCubic)
            }
        },
        targetValueByState = {
            when (it) {
                false -> 0f
                true -> 1f
            }
        },
    )

    val animatePathKotlin by visible.animateFloat(
        transitionSpec = {
            when (targetState) {
                true -> tween(durationMillis = 800, delayMillis = 1400, easing = EaseInCubic)
                false -> tween(durationMillis = 400, delayMillis = 0, easing = EaseOutCubic)
            }
        },
        targetValueByState = {
            when (it) {
                false -> 0f
                true -> 1f
            }
        },
    )

    Group(name = "inside-paths") {
        Path(
            stroke = paint0_linear_12_2,
            strokeLineWidth = 4f,
            strokeAlpha = birdAlpha,
            trimPathEnd = animatePath,
            pathData = PathData {
                moveTo(706.35f, 281.41f)
                verticalLineTo(23.31f)
            },
        )
        Path(
            stroke = paint1_linear_12_2,
            strokeLineWidth = 4f,
            strokeAlpha = birdAlpha,
            trimPathEnd = animatePath,
            pathData = PathData {
                moveTo(568.06f, 16.31f)
                verticalLineTo(290.76f)
                curveTo(568.06f, 326.2f, 594.67f, 355.41f, 628.97f, 359.48f)
            },
        )
        Path(
            stroke = paint2_linear_12_2,
            strokeLineWidth = 4f,
            strokeAlpha = birdAlpha,
            trimPathEnd = animatePath,
            pathData = PathData {
                moveTo(705.26f, 697.15f)
                curveTo(702.26f, 692.03f, 698.56f, 687.22f, 694.18f, 682.84f)
                lineTo(550.63f, 539.12f)
                curveTo(522.6f, 511.06f, 477.16f, 511.06f, 449.13f, 539.12f)
                curveTo(439.04f, 549.22f, 432.6f, 561.58f, 429.77f, 574.58f)
            },
        )
        Path(
            stroke = paint3_linear_12_2,
            strokeLineWidth = 4f,
            strokeAlpha = birdAlpha,
            trimPathEnd = animatePath,
            pathData = PathData {
                moveTo(568.05f, 290.76f)
                curveTo(568.05f, 252.53f, 537.1f, 221.54f, 498.91f, 221.54f)
                curveTo(460.73f, 221.54f, 429.77f, 252.53f, 429.77f, 290.76f)
                horizontalLineTo(568.07f)
                horizontalLineTo(568.05f)
                close()
            },
        )
        Path(
            stroke = paint4_linear_12_2,
            strokeLineWidth = 4f,
            strokeAlpha = birdAlpha,
            trimPathEnd = animatePath,
            pathData = PathData {
                moveTo(704.9f, 697.32f)
                curveTo(692.2f, 675.31f, 668.44f, 660.5f, 641.22f, 660.5f)
                curveTo(610.66f, 660.5f, 584.46f, 679.19f, 573.39f, 705.76f)
            },
        )
        Path(
            stroke = paint5_linear_12_2,
            strokeLineWidth = 4f,
            strokeAlpha = birdAlpha,
            trimPathEnd = animatePath,
            pathData = PathData {
                moveTo(490.16f, 82.69f)
                curveTo(492.53f, 82.94f, 494.94f, 83.07f, 497.38f, 83.07f)
                curveTo(534.54f, 83.07f, 564.85f, 53.72f, 566.45f, 16.9f)
            },
        )
        Path(
            stroke = paint6_linear_12_2,
            strokeLineWidth = 4f,
            strokeAlpha = birdAlpha,
            trimPathEnd = animatePath,
            pathData = PathData {
                moveTo(429.77f, 142.57f)
                verticalLineTo(220.15f)
                curveTo(429.77f, 220.91f, 430.38f, 221.53f, 431.14f, 221.53f)
                horizontalLineTo(566.67f)
                curveTo(567.43f, 221.53f, 568.05f, 220.91f, 568.05f, 220.15f)
                verticalLineTo(84.46f)
                curveTo(568.05f, 83.7f, 567.43f, 83.08f, 566.67f, 83.08f)
                horizontalLineTo(489.47f)
            },
        )
        Path(
            stroke = paint7_linear_12_2,
            strokeLineWidth = 4f,
            strokeAlpha = birdAlpha,
            trimPathEnd = animatePath,
            pathData = PathData {
                moveTo(629.22f, 358.94f)
                horizontalLineTo(569.87f)
                curveTo(569.1f, 358.94f, 568.49f, 359.56f, 568.49f, 360.33f)
                verticalLineTo(496.67f)
                curveTo(568.49f, 497.44f, 569.1f, 498.05f, 569.87f, 498.05f)
                horizontalLineTo(704.99f)
                curveTo(705.75f, 498.05f, 706.36f, 497.44f, 706.36f, 496.67f)
                verticalLineTo(437.35f)
            },
        )
        Path(
            stroke = paint8_linear_12_2,
            strokeLineWidth = 4f,
            strokeAlpha = birdAlpha,
            trimPathEnd = animatePath,
            pathData = PathData {
                moveTo(350.01f, 497.24f)
                curveTo(397.13f, 475.17f, 429.78f, 427.29f, 429.78f, 371.77f)
                verticalLineTo(221.53f)
                horizontalLineTo(350.91f)
            },
        )
        Path(
            stroke = paint9_linear_12_2,
            strokeLineWidth = 4f,
            strokeAlpha = birdAlpha,
            trimPathEnd = animatePath,
            pathData = PathData {
                moveTo(429.29f, 575.86f)
                curveTo(429.61f, 573.16f, 429.78f, 570.42f, 429.78f, 567.65f)
                curveTo(429.78f, 529.42f, 398.82f, 498.43f, 360.64f, 498.43f)
                horizontalLineTo(351.63f)
            },
        )
        Path(
            stroke = paint11_linear_12_2,
            strokeLineWidth = 4f,
            strokeAlpha = birdAlpha,
            trimPathEnd = animatePath,
            pathData = PathData {
                moveTo(83.46f, 66.22f)
                curveTo(95.6f, 76.78f, 110.97f, 83.18f, 128.31f, 83.18f)
                horizontalLineTo(342.22f)
            },
        )
        Path(
            stroke = paint12_linear_12_2,
            strokeLineWidth = 4f,
            strokeAlpha = birdAlpha,
            trimPathEnd = animatePath,
            pathData = PathData {
                moveTo(45.01f, 192.16f)
                lineTo(174.21f, 62.81f)
                curveTo(190.92f, 46.08f, 197.27f, 23.62f, 193.31f, 2f)
            },
        )
        Path(
            stroke = paint13_linear_12_2,
            strokeLineWidth = 4f,
            strokeAlpha = birdAlpha,
            trimPathEnd = animatePath,
            pathData = PathData {
                moveTo(74.16f, 220.6f)
                verticalLineTo(139.55f)
                curveTo(74.16f, 122.46f, 68.42f, 107.29f, 58.17f, 95.21f)
            },
        )
        Path(
            stroke = paint14_radial_12_2,
            strokeLineWidth = 4f,
            strokeAlpha = birdAlpha,
            trimPathEnd = animatePath,
            pathData = PathData {
                moveTo(338.96f, 221.53f)
                horizontalLineTo(155.95f)
                verticalLineTo(286.83f)
            },
        )
        Path(
            stroke = paint15_linear_12_2,
            strokeLineWidth = 4f,
            strokeAlpha = birdAlpha,
            trimPathEnd = animatePath,
            pathData = PathData {
                moveTo(777.21f, 566.25f)
                lineTo(706.78f, 635.53f)
                curveTo(706.53f, 635.79f, 706.38f, 636.14f, 706.38f, 636.51f)
                verticalLineTo(696.88f)
            },
        )
        Path(
            stroke = paint16_linear_12_2,
            strokeLineWidth = 4f,
            strokeAlpha = birdAlpha,
            trimPathEnd = animatePath,
            pathData = PathData {
                moveTo(759.35f, 498.42f)
                horizontalLineTo(706.35f)
                curveTo(706.35f, 536.65f, 737.31f, 567.64f, 775.49f, 567.64f)
                curveTo(776.75f, 567.64f, 776.76f, 567.6f, 778f, 567.53f)
            },
        )
        Path(
            stroke = paint20_linear_26_812,
            strokeLineWidth = 4f,
            strokeAlpha = birdAlpha,
            trimPathEnd = animatePath,
            pathData = PathData {
                moveTo(498.91f, 290.75f)
                curveTo(460.73f, 290.75f, 429.77f, 321.74f, 429.77f, 359.97f)
                curveTo(429.77f, 444.79f, 429.77f, 492.34f, 429.77f, 577.16f)
            },
        )
        Path(
            stroke = paint21_linear_26_812,
            strokeLineWidth = 4f,
            strokeAlpha = birdAlpha,
            trimPathEnd = animatePath,
            pathData = PathData {
                moveTo(513.16f, 660.3f)
                curveTo(544.51f, 653.72f, 568.05f, 625.91f, 568.05f, 592.57f)
                verticalLineTo(359.97f)
                curveTo(568.05f, 321.74f, 537.1f, 290.75f, 498.91f, 290.75f)
            },
        )
        Path(
            stroke = paint18_linear_12_2,
            strokeLineWidth = 4f,
            strokeAlpha = birdAlpha,
            trimPathEnd = animatePath,
            pathData = PathData {
                moveTo(756.59f, 72.76f)
                curveTo(727.59f, 81f, 706.35f, 107.69f, 706.35f, 139.37f)
                curveTo(706.35f, 174.25f, 732.13f, 203.09f, 765.65f, 207.88f)
            },
        )
    }
    Group(name = "head", translationX = birdHeadTranslation) {
        Path(
            stroke = paint19_linear_12_2,
            strokeLineWidth = 4f,
            strokeAlpha = birdAlpha,
            pathData = PathData {
                moveTo(210.14f, 296.01f)
                curveTo(171.02f, 296.01f, 134.24f, 280.75f, 106.57f, 253.06f)
                lineTo(3f, 149.36f)
                lineTo(106.58f, 45.66f)
                curveTo(134.24f, 17.97f, 171.02f, 2.71f, 210.14f, 2.71f)
                curveTo(249.26f, 2.71f, 286.05f, 17.97f, 313.72f, 45.66f)
                curveTo(370.83f, 102.84f, 370.83f, 195.88f, 313.72f, 253.06f)
                curveTo(286.05f, 280.75f, 249.27f, 296.01f, 210.14f, 296.01f)
                close()
            },
        )
    }
    Group(name = "body", translationX = birdBodyTranslation) {
        Path(
            stroke = paint20_linear_12_2,
            strokeLineWidth = 4f,
            strokeAlpha = birdAlpha,
            pathData = PathData {
                moveTo(630.64f, 717f)
                curveTo(591.52f, 717f, 554.73f, 701.74f, 527.06f, 674.05f)
                lineTo(316.89f, 463.63f)
                curveTo(291f, 437.72f, 275.82f, 403.39f, 274.09f, 366.9f)
                curveTo(272.12f, 325.69f, 287.7f, 285.32f, 316.82f, 256.16f)
                lineTo(317.02f, 255.96f)
                lineTo(527.07f, 45.69f)
                curveTo(554.73f, 17.99f, 591.52f, 2.73f, 630.65f, 2.73f)
                curveTo(669.78f, 2.73f, 706.55f, 17.99f, 734.22f, 45.69f)
                curveTo(761.88f, 73.38f, 777.13f, 110.21f, 777.13f, 149.38f)
                curveTo(777.13f, 188.56f, 761.88f, 225.38f, 734.22f, 253.08f)
                lineTo(627.56f, 359.87f)
                lineTo(734.22f, 466.65f)
                curveTo(761.88f, 494.35f, 777.13f, 531.17f, 777.13f, 570.35f)
                curveTo(777.13f, 609.53f, 761.88f, 646.34f, 734.22f, 674.05f)
                curveTo(706.56f, 701.75f, 669.78f, 717f, 630.65f, 717f)
                horizontalLineTo(630.64f)
                close()
            },
        )
    }

    Path(
        stroke = paint10_linear_12_2,
        strokeLineWidth = 4f,
        strokeAlpha = birdAlpha,
        trimPathEnd = animatePathKotlin,
        pathData = PathData {
            moveTo(155.953f, 221.398f)
            verticalLineTo(82.88f)
            horizontalLineTo(295.214f)
            lineTo(225.643f, 152.209f)
            lineTo(295.214f, 221.43f)
            horizontalLineTo(155.953f)
        }
    )
}

private val paint0_linear_12_2 = Brush.linearGradient(
    colorStops = arrayOf(
        0f to CONFERENCE_YELLOW,
        0.4f to CONFERENCE_RED,
        1f to CONFERENCE_PURPLE
    ),
    start = Offset(53.03f, 152.61f),
    end = Offset(54.46f, 159.5f),
)

private val paint1_linear_12_2 = Brush.linearGradient(
    colorStops = arrayOf(
        0f to CONFERENCE_YELLOW,
        0.4f to CONFERENCE_RED,
        0.63f to Color(0xFFEE01B4).copy(alpha = 0.0f),
        0.86f to Color(0xFFED01C1),
        0.91f to Color(0xFFEA00DD),
        1f to CONFERENCE_PURPLE
    ),
    start = Offset(5.34f, 151.53f),
    end = Offset(154.65f, 384.52f),
)

private val paint2_linear_12_2 = Brush.linearGradient(
    colorStops = arrayOf(
        0f to CONFERENCE_YELLOW,
        0.4f to CONFERENCE_RED,
        0.64f to Color(0xFFF70169).copy(alpha = 0.0f),
        0.68f to Color(0xFFF4017E),
        0.81f to Color(0xFFF001A4),
        0.85f to Color(0xFFEE01B3).copy(alpha = 0.0f),
        0.88f to Color(0xFFED01C0),
        1f to CONFERENCE_PURPLE
    ),
    start = Offset(4.26f, 150.45f),
    end = Offset(696.37f, 189.26f),
)

private val paint3_linear_12_2 = Brush.linearGradient(
    colorStops = arrayOf(
        0f to CONFERENCE_YELLOW,
        0.62f to CONFERENCE_RED.copy(alpha = 0.0f),
        0.66f to Color(0xFFF40181),
        1f to CONFERENCE_PURPLE
    ),
    start = Offset(0.82f, 112.14f),
    end = Offset(705.15f, 176.54f),
)

private val paint4_linear_12_2 = Brush.linearGradient(
    colorStops = arrayOf(
        0f to CONFERENCE_YELLOW,
        0.4f to CONFERENCE_RED,
        0.65f to Color(0xFFF5017B).copy(alpha = 0.0f),
        0.67f to Color(0xFFF1019A),
        0.76f to Color(0xFFEC01C6),
        0.79f to Color(0xFFEA00D7).copy(alpha = 0.0f),
        1f to CONFERENCE_PURPLE
    ),
    start = Offset(-1.66f, 152.07f),
    end = Offset(791.22f, 76.84f),
)

private val paint5_linear_12_2 = Brush.linearGradient(
    colorStops = arrayOf(
        0f to CONFERENCE_YELLOW,
        0.4f to CONFERENCE_RED,
        0.71f to Color(0xFFF3018D).copy(alpha = 0.0f),
        0.8f to Color(0xFFF001A9),
        1f to CONFERENCE_PURPLE
    ),
    start = Offset(3.18f, 150.45f),
    end = Offset(675.78f, 252.97f),
)

private val paint6_linear_12_2 = Brush.linearGradient(
    colorStops = arrayOf(
        0f to CONFERENCE_YELLOW,
        0.34f to CONFERENCE_RED,
        0.63f to Color(0xFFF70169).copy(alpha = 0.0f),
        0.67f to Color(0xFFF40181),
        0.96f to CONFERENCE_PURPLE
    ),
    start = Offset(27.74f, -62.21f),
    end = Offset(490.87f, 419.97f),
)

private val paint7_linear_12_2 = Brush.linearGradient(
    colorStops = arrayOf(
        0f to CONFERENCE_YELLOW.copy(alpha = 0.0f),
        0.35f to CONFERENCE_RED.copy(alpha = 0.0f),
        0.56f to Color(0xFFEC00CC),
        0.82f to Color(0xFFED01BB).copy(alpha = 0.0f),
        0.89f to CONFERENCE_PURPLE
    ),
    start = Offset(21.7f, 217.73f),
    end = Offset(685.31f, 223.15f),
)

private val paint8_linear_12_2 = Brush.linearGradient(
    colorStops = arrayOf(
        0f to CONFERENCE_YELLOW.copy(alpha = 0.0f),
        0.16f to CONFERENCE_RED.copy(alpha = 0.0f),
        0.24f to Color(0xFFFB0241).copy(alpha = 0.0f),
        0.3f to Color(0xFFF90156),
        0.69f to CONFERENCE_PURPLE
    ),
    start = Offset(275.83f, 322.99f),
    end = Offset(589.6f, 336.12f),
)

private val paint9_linear_12_2 = Brush.linearGradient(
    colorStops = arrayOf(
        0f to CONFERENCE_YELLOW.copy(alpha = 0.0f),
        0.62f to CONFERENCE_RED.copy(alpha = 0.0f),
        0.68f to Color(0xFFF6016C),
        1f to CONFERENCE_PURPLE
    ),
    start = Offset(-1.66f, 149.37f),
    end = Offset(653.63f, 285.44f),
)

private val paint10_linear_12_2 = Brush.linearGradient(
    colorStops = arrayOf(
        0.0474716f to CONFERENCE_YELLOW,
        0.102406f to CONFERENCE_YELLOW,
        0.398382f to CONFERENCE_RED,
        1f to CONFERENCE_PURPLE
    ),
    start = Offset(-1.70171f, 152.307f),
    end = Offset(658.021f, 284.949f),
)

private val paint11_linear_12_2 = Brush.linearGradient(
    colorStops = arrayOf(
        0.15f to CONFERENCE_YELLOW,
        0.26f to Color(0xFFFF801A).copy(alpha = 0.0f),
        0.4f to CONFERENCE_RED.copy(alpha = 0.0f),
        0.48f to Color(0xFFFD022E),
        1f to CONFERENCE_PURPLE
    ),
    start = Offset(3.18f, 150.45f),
    end = Offset(584.84f, -192.23f),
)

private val paint12_linear_12_2 = Brush.linearGradient(
    colorStops = arrayOf(
        0f to CONFERENCE_YELLOW,
        0.4f to CONFERENCE_RED,
        1f to CONFERENCE_PURPLE
    ),
    start = Offset(-2.05f, 145.06f),
    end = Offset(609.05f, 343.57f),
)

private val paint13_linear_12_2 = Brush.linearGradient(
    colorStops = arrayOf(
        0f to CONFERENCE_YELLOW,
        0.22f to CONFERENCE_YELLOW.copy(alpha = 0.0f),
        0.4f to CONFERENCE_RED,
        1f to CONFERENCE_PURPLE
    ),
    start = Offset(6.96f, 150.45f),
    end = Offset(100.69f, 335.21f),
)

private val paint14_radial_12_2 = Brush.radialGradient(
    colorStops = arrayOf(
        0f to CONFERENCE_YELLOW,
        0.22f to Color(0xFFFF451B),
        0.38f to CONFERENCE_RED.copy(alpha = 0.0f),
        0.45f to Color(0xFFFE0227),
        1f to CONFERENCE_PURPLE
    ),
    center = Offset(-1.31f, 149.2f),
    radius = 722.57f
)

private val paint15_linear_12_2 = Brush.linearGradient(
    colorStops = arrayOf(
        0f to CONFERENCE_YELLOW,
        0.4f to CONFERENCE_RED,
        0.66f to Color(0xFFEA00DF).copy(alpha = 0.0f),
        0.69f to Color(0xFFE800E9),
        0.94f to CONFERENCE_PURPLE
    ),
    start = Offset(227.04f, 775.74f),
    end = Offset(949.27f, 670.54f),
)

private val paint16_linear_12_2 = Brush.linearGradient(
    colorStops = arrayOf(
        0f to CONFERENCE_YELLOW,
        0.4f to CONFERENCE_RED,
        0.87f to Color(0xFFEA00D9).copy(alpha = 0.0f),
        0.91f to Color(0xBEE900E5),
        0.93f to Color(0xFFE800E9),
        0.93f to CONFERENCE_PURPLE
    ),
    start = Offset(1.57f, 150.99f),
    end = Offset(832.73f, 467.89f),
)

private val paint20_linear_26_812 = Brush.linearGradient(
    colorStops = arrayOf(
        0f to CONFERENCE_YELLOW,
        0.4f to CONFERENCE_RED.copy(alpha = 0.0f),
        0.62f to Color(0xFFF70167).copy(alpha = 0.0f),
        0.73f to Color(0xFFF40182),
        1f to CONFERENCE_PURPLE
    ),
    start = Offset(115.83f, 351.92f),
    end = Offset(592.2f, 359.18f),
)

private val paint21_linear_26_812 = Brush.linearGradient(
    colorStops = arrayOf(
        0f to CONFERENCE_YELLOW,
        0.4f to CONFERENCE_RED.copy(alpha = 0.0f),
        0.62f to Color(0xFFF70167).copy(alpha = 0.0f),
        0.73f to Color(0xFFF40182),
        1f to CONFERENCE_PURPLE
    ),
    start = Offset(115.83f, 351.92f),
    end = Offset(592.2f, 359.18f),
)

private val paint18_linear_12_2 = Brush.linearGradient(
    colorStops = arrayOf(
        0f to CONFERENCE_YELLOW,
        0.4f to CONFERENCE_RED,
        0.69f to Color(0xFFEA00DA).copy(alpha = 0.0f),
        0.95f to CONFERENCE_PURPLE
    ),
    start = Offset(10.55f, 96.1f),
    end = Offset(360.68f, 535.02f),
)

private val paint19_linear_12_2 = Brush.linearGradient(
    colorStops = arrayOf(
        0f to CONFERENCE_YELLOW,
        0.4f to CONFERENCE_RED,
        1f to CONFERENCE_PURPLE
    ),
    start = Offset(3.19f, 149.37f),
    end = Offset(676.78f, 244.26f),
)

private val paint20_linear_12_2 = Brush.linearGradient(
    colorStops = arrayOf(
        0f to CONFERENCE_YELLOW,
        0.4f to CONFERENCE_RED,
        1f to CONFERENCE_PURPLE
    ),
    start = Offset(11.26f, 146.68f),
    end = Offset(584.45f, 361.87f),
)
