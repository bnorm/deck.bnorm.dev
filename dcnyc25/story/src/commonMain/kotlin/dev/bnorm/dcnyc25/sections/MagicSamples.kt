package dev.bnorm.dcnyc25.sections

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import dev.bnorm.dcnyc25.highlight
import dev.bnorm.dcnyc25.template.Full
import dev.bnorm.dcnyc25.template.TextSurface
import dev.bnorm.dcnyc25.template.code1
import dev.bnorm.deck.shared.INTELLIJ_LIGHT
import dev.bnorm.deck.shared.code.CodeSample
import dev.bnorm.deck.shared.code.buildCodeSamples
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.LocalSceneMode
import dev.bnorm.storyboard.easel.SceneMode
import dev.bnorm.storyboard.easel.assist.SceneCaption
import dev.bnorm.storyboard.easel.template.*
import dev.bnorm.storyboard.text.highlight.CodeScope
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.text.splitByTags
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

fun StoryboardBuilder.MagicSamples() {
    scene(
        stateCount = 1,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        Full(MaterialTheme.colors.primary) {
            val index by animateSampleIndex(
                samples = PowerAssertSamples,
                defaultDelay = 2.seconds,
                name = "Power-Assert",
            )

            Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(16.dp)) {
                TextSurface {
                    ProvideTextStyle(MaterialTheme.typography.code1) {
                        MagicText(
                            PowerAssertSamples[index].string,
                            modifier = Modifier.fillMaxSize().padding(16.dp)
                        )
                    }
                }
            }
        }
    }
    scene(
        stateCount = 1,
        enterTransition = enter(
            start = SceneEnter(alignment = Alignment.CenterEnd),
            end = SceneEnter(alignment = Alignment.BottomCenter),
        ),
        exitTransition = exit(
            start = SceneExit(alignment = Alignment.CenterEnd),
            end = SceneExit(alignment = Alignment.BottomCenter),
        )
    ) {
        Full(MaterialTheme.colors.secondary) {
            val index by animateSampleIndex(
                samples = ComposeSamples,
                name = "Compose",
            )

            Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(16.dp)) {
                TextSurface {
                    ProvideTextStyle(MaterialTheme.typography.code1) {
                        MagicText(
                            ComposeSamples[index].string.splitByTags(),
                            modifier = Modifier.fillMaxSize().padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun animateSampleIndex(
    samples: List<CodeSample>,
    previewIndex: Int = samples.size - 1,
    defaultDelay: Duration = 3.seconds,
    name: String? = null,
): State<Int> {
    val size = samples.size

    // When rendering the scene for preview, render the finished state and do not animate the sample.
    val isStory = LocalSceneMode.current == SceneMode.Story
    var playing by remember { mutableStateOf(isStory) }
    val index = remember(size) { mutableIntStateOf(if (isStory) 0 else previewIndex) }

    SceneCaption {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (name != null) {
                Text("Playing: $name")
            } else {
                Text("Playing")
            }
            Spacer(Modifier.width(16.dp))
            Switch(playing, onCheckedChange = { playing = it })
            if (!playing) {
                IconButton(onClick = {
                    index.value = (if (index.value <= 0) size else index.value) - 1
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Previous",
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                    )
                }
                IconButton(onClick = {
                    index.value = (index.value + 1) % size
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = "Next",
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                    )
                }
            }
        }
    }

    StoryEffect(playing) {
        while (playing) {
            val data = samples[index.value].data
            delay(data as? Duration ?: defaultDelay)
            index.value = (index.value + 1) % size
        }
    }

    return index
}

private val PowerAssertSamples = buildCodeSamples {
    val samples = listOf(
        """
            assert(str.length >= 1 && str[0] == 'x')
        """.trimIndent(),
        """
            val tmp1 = str
            assert(tmp1.length >= 1 && str[0] == 'x')
        """.trimIndent(),
        """
            val tmp1 = str
            val tmp2 = tmp1.length
            assert(tmp2 >= 1 && str[0] == 'x')
        """.trimIndent(),
        """
            val tmp1 = str
            val tmp2 = tmp1.length
            val tmp3 = tmp2 >= 1
            assert(tmp3 && str[0] == 'x')
        """.trimIndent(),
        """
            val tmp1 = str
            val tmp2 = tmp1.length
            val tmp3 = tmp2 >= 1
            assert(when {
                tmp3 -> str[0] == 'x'
                else -> false
            })
        """.trimIndent(),
        """
            val tmp1 = str
            val tmp2 = tmp1.length
            val tmp3 = tmp2 >= 1
            when {
                tmp3 -> assert(str[0] == 'x')
                else -> assert(false)
            }
        """.trimIndent(),
        """
            val tmp1 = str
            val tmp2 = tmp1.length
            val tmp3 = tmp2 >= 1
            when {
                tmp3 -> {
                    val tmp4 = str
                    assert(tmp4[0] == 'x')
                }
                else -> assert(false)
            }
        """.trimIndent(),
        """
            val tmp1 = str
            val tmp2 = tmp1.length
            val tmp3 = tmp2 >= 1
            when {
                tmp3 -> {
                    val tmp4 = str
                    val tmp5 = tmp4[0]
                    assert(tmp5 == 'x')
                }
                else -> assert(false)
            }
        """.trimIndent(),
        """
            val tmp1 = str
            val tmp2 = tmp1.length
            val tmp3 = tmp2 >= 1
            when {
                tmp3 -> {
                    val tmp4 = str
                    val tmp5 = tmp4[0]
                    val tmp6 = tmp5 == 'x'
                    assert(tmp6)
                }
                else -> assert(false)
            }
        """.trimIndent(),
        """
            val tmp1 = str
            val tmp2 = tmp1.length
            val tmp3 = tmp2 >= 1
            when {
                tmp3 -> {
                    val tmp4 = str
                    val tmp5 = tmp4[0]
                    val tmp6 = tmp5 == 'x'
                    assert(tmp6)
                }
                else -> assert(false) {
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
    )

    val m by tag("message")
    val finalExample = """
        val tmp1 = str
        val tmp2 = tmp1.length
        val tmp3 = tmp2 >= 1
        when {
            tmp3 -> {
                val tmp4 = str
                val tmp5 = tmp4[0]
                val tmp6 = tmp5 == 'x'
                assert(tmp6) {$m
                    "${'"'}"
                        assert(str.length >= 1 && str[0] == 'x')
                               |   |      |       |  |   |
                               |   |      |       |  |   ${'$'}tmp6
                               |   |      |       |  ${'$'}tmp5
                               |   |      |       ${'$'}tmp4
                               |   |      ${'$'}tmp3
                               |   ${'$'}tmp2
                               ${'$'}tmp1
                    "${'"'}".trimIndent()
                $m}
            }
            else -> assert(false) {$m
                "${'"'}"
                    assert(str.length >= 1 && str[0] == 'x')
                           |   |      |
                           |   |      ${'$'}tmp3
                           |   ${'$'}tmp2
                           ${'$'}tmp1
                "${'"'}".trimIndent()
            $m}
        }
    """.trimIndent()

    fun String.toCodeSample(): CodeSample {
        return CodeSample(lazy {
            extractTags(this).highlight(
                scope = CodeScope.Function,
                identifierStyle = { identifier ->
                    when (identifier) {
                        "assert" -> INTELLIJ_LIGHT.staticFunctionCall
                        "trimIndent" -> INTELLIJ_LIGHT.extensionFunctionCall
                        "length" -> INTELLIJ_LIGHT.property
                        else -> null
                    }
                }
            )
        })
    }

    samples.map { it.toCodeSample() }
        .instead { attach(5.seconds) }
        .then { finalExample.toCodeSample().attach(5.seconds) }
        .then { collapse(m).attach(3.seconds) }
}

private val ComposeSamples = buildCodeSamples {
    fun String.toCodeSample(): CodeSample {
        return CodeSample(lazy {
            extractTags(this).highlight(identifierStyle = {
                when (it) {
                    "skipping" -> INTELLIJ_LIGHT.property
                    "mutableStateOf" -> INTELLIJ_LIGHT.extensionFunctionCall
                    // Mutable variable.
                    "count" -> INTELLIJ_LIGHT.simple + SpanStyle(textDecoration = TextDecoration.Underline)
                    // Compose function call.
                    "remember", "MyButton", "MyCounter" -> SpanStyle(color = Color(0xFF009900))
                    else -> null
                }
            })
        })
    }

    val s by tag("splitter") // TODO support a "point" tag?

    val c1 by tag("composer")
    val c2 by tag("composer calls")
    val c3 by tag("changed")
    val c4 by tag("check skip")
    val c5 by tag("update scope")

    val basic = """
        @Composable fun MyCounter(${c1}composer: Composer${c1}${c3}, changed: Int${c3}) {${c2}
          composer.startRestartGroup(123)${c2}
          var count by remember(${c1}composer${c1}${c3}, 0${c3}) { mutableStateOf(0) }
          MyButton(
            text = "Count: ${'$'}count",
            onPress = { count += 1 },${c1}
            composer = composer,${c1}${c3}
            changed = 0,${c3}
          )${c2}
          composer.endRestartGroup()${c2}
        }
    """.trimIndent().toCodeSample()

    val restartable = """
        @Composable fun MyCounter($s${s}composer: Composer$s${s}, changed: Int$s${s}) {
          composer.startRestartGroup(123)
          ${c4}if (changed == 0 && composer.skipping) {
            composer.skipToGroupEnd()
          }${c4} else {
            var count by remember($s${s}composer$s${s}, 0$s${s}) { mutableStateOf(0) }
            MyButton(
              text = "Count: ${'$'}count",
              onPress = { count += 1 },
              composer = composer,
              changed = 0,
            )
          }
          composer.endRestartGroup()${c5}?.let {
            it.updateScope { composer, _ -> MyCounter(composer, changed or 1) }
          }${c5}
        }
    """.trimIndent().toCodeSample()

    basic.hide(c1, c2, c3)
        .then { reveal(c1).focus(c1) }
        .then { reveal(c2).focus(c2) }
        .then { reveal(c3).focus(c3) }
        .then { restartable.hide(c5).focus(c4) }
        .then { reveal(c5).focus(c5) }
        .then { unfocus().attach(5.seconds) }
}
