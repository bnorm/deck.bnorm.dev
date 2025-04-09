package dev.bnorm.kc25.sections.existing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.deck.shared.mac.MacTerminal
import dev.bnorm.kc25.components.temp.BULLET_1
import dev.bnorm.kc25.template.*
import dev.bnorm.kc25.template.code.buildCodeSamples
import dev.bnorm.storyboard.DisplayType
import dev.bnorm.storyboard.LocalDisplayType
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.RevealEach
import dev.bnorm.storyboard.easel.template.StoryEffect
import dev.bnorm.storyboard.easel.template.section
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.text.splitByTags
import dev.bnorm.storyboard.toState
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

private val SAMPLES = buildCodeSamples {
    val p by tag("print")
    val s1 by tag("sortBy1")
    val a1 by tag("addId1")
    val a2 by tag("add1")
    val s2 by tag("sortBy2")
    val a3 by tag("addId2")
    val a4 by tag("add2")
    val s3 by tag("sortBy3")

    val base = """
        (@Import DataFrame.readCSV("jetbrains_repositories.csv"))
            .convert { topics }.with { it.removeSurrounding("[", "]").split(", ") }$s1
            .sortBy { watchers.desc() }$s1$a1
            .addId("watchersRank")$a1$a2
            .add { "topicsCount" from { topics.size } }.remove { topics }$a2$s2
            .sortBy { topicsCount.desc() }$s2$a3
            .addId("topicsRank")$a3$a4
            .add { "promotionScore" from { watchersRank + topicsRank } }$a4$s3
            .sortBy { promotionScore.desc() }$s3$p
            .print()$p
    """.trimIndent()
        .toCodeSample(
            codeStyle = INTELLIJ_DARK_CODE_STYLE,
            identifierType = { highlighting, string ->
                when (string) {
                    "readCSV", "convert", "with", "removeSurrounding", "split", "sortBy", "addId", "add", "print", "desc", "from", "remove" -> highlighting.extensionFunctionCall
                    "watchers", "watchersRank", "topics", "topicsCount", "topicsRank", "promotionScore" -> highlighting.staticProperty
                    "size" -> highlighting.property
                    else -> null
                }
            }
        )

    base.hide(p, s1, a1, a2, s2, a3, a4, s3)
        .then { reveal(p).focus(p) }
        .then { reveal(s1).focus(s1) }
        .then { reveal(a1).focus(a1) }
        .then { reveal(a2).focus(a2) }
        .then { reveal(s2).focus(s2) }
        .then { reveal(a3).focus(a3) }
        .then { reveal(a4).focus(a4) }
        .then { reveal(s3).focus(s3) }
        .then { unfocus() }
}

private val OUTPUT = listOf(
    "\n".repeat(10),
    """
                               name watchers                                   topics
     0            JetBrains/ideavim     6120 [ideavim, intellij, intellij-platform...
     1 JetBrains/intellij-community    12926 [code-editor, ide, intellij, intellij...
     2             JetBrains/kotlin    39402 [compiler, gradle-plugin, intellij-pl...
     3            JetBrains/Exposed     5688                  [dao, kotlin, orm, sql]
     4      JetBrains/kotlin-native     7101 [c, compiler, kotlin, llvm, objective-c]
     5            JetBrains/ring-ui     2836        [components, jetbrains-ui, react]
     6     JetBrains/kotlinconf-app     2628                                       []
     7      JetBrains/JetBrainsMono     6059 [coding-font, font, ligatures, monosp...
    """.trimIndent(),
    """
                               name watchers                                   topics
     0             JetBrains/kotlin    39402 [compiler, gradle-plugin, intellij-pl...
     1 JetBrains/intellij-community    12926 [code-editor, ide, intellij, intellij...
     2      JetBrains/kotlin-native     7101 [c, compiler, kotlin, llvm, objective-c]
     3            JetBrains/ideavim     6120 [ideavim, intellij, intellij-platform...
     4      JetBrains/JetBrainsMono     6059 [coding-font, font, ligatures, monosp...
     5            JetBrains/Exposed     5688                  [dao, kotlin, orm, sql]
     6            JetBrains/ring-ui     2836        [components, jetbrains-ui, react]
     7     JetBrains/kotlinconf-app     2628                                       []
    """.trimIndent(),
    """
       watchersRank                         name watchers                                   topics
     0            0             JetBrains/kotlin    39402 [compiler, gradle-plugin, intellij-pl...
     1            1 JetBrains/intellij-community    12926 [code-editor, ide, intellij, intellij...
     2            2      JetBrains/kotlin-native     7101 [c, compiler, kotlin, llvm, objective-c]
     3            3            JetBrains/ideavim     6120 [ideavim, intellij, intellij-platform...
     4            4      JetBrains/JetBrainsMono     6059 [coding-font, font, ligatures, monosp...
     5            5            JetBrains/Exposed     5688                  [dao, kotlin, orm, sql]
     6            6            JetBrains/ring-ui     2836        [components, jetbrains-ui, react]
     7            7     JetBrains/kotlinconf-app     2628                                       []
    """.trimIndent(),
    """
       watchersRank                         name watchers topicsCount
     0            0             JetBrains/kotlin    39402           7
     1            1 JetBrains/intellij-community    12926           5
     2            2      JetBrains/kotlin-native     7101           5
     3            3            JetBrains/ideavim     6120           7
     4            4      JetBrains/JetBrainsMono     6059           6
     5            5            JetBrains/Exposed     5688           4
     6            6            JetBrains/ring-ui     2836           3
     7            7     JetBrains/kotlinconf-app     2628           1
    """.trimIndent(),
    """
       watchersRank                         name watchers topicsCount
     0            0             JetBrains/kotlin    39402           7
     1            3            JetBrains/ideavim     6120           7
     2            4      JetBrains/JetBrainsMono     6059           6
     3            1 JetBrains/intellij-community    12926           5
     4            2      JetBrains/kotlin-native     7101           5
     5            5            JetBrains/Exposed     5688           4
     6            6            JetBrains/ring-ui     2836           3
     7            7     JetBrains/kotlinconf-app     2628           1
    """.trimIndent(),
    """
       topicsRank watchersRank                         name watchers topicsCount
     0          0            0             JetBrains/kotlin    39402           7
     1          1            3            JetBrains/ideavim     6120           7
     2          2            4      JetBrains/JetBrainsMono     6059           6
     3          3            1 JetBrains/intellij-community    12926           5
     4          4            2      JetBrains/kotlin-native     7101           5
     5          5            5            JetBrains/Exposed     5688           4
     6          6            6            JetBrains/ring-ui     2836           3
     7          7            7     JetBrains/kotlinconf-app     2628           1
    """.trimIndent(),
    """
       topicsRank watchersRank                         name watchers topicsCount promotionScore
     0          0            0             JetBrains/kotlin    39402           7              0
     1          1            3            JetBrains/ideavim     6120           7              4
     2          2            4      JetBrains/JetBrainsMono     6059           6              6
     3          3            1 JetBrains/intellij-community    12926           5              4
     4          4            2      JetBrains/kotlin-native     7101           5              6
     5          5            5            JetBrains/Exposed     5688           4             10
     6          6            6            JetBrains/ring-ui     2836           3             12
     7          7            7     JetBrains/kotlinconf-app     2628           1             14
    """.trimIndent(),
    """
       topicsRank watchersRank                         name watchers topicsCount promotionScore
     0          7            7     JetBrains/kotlinconf-app     2628           1             14
     1          6            6            JetBrains/ring-ui     2836           3             12
     2          5            5            JetBrains/Exposed     5688           4             10
     3          2            4      JetBrains/JetBrainsMono     6059           6              6
     4          4            2      JetBrains/kotlin-native     7101           5              6
     5          1            3            JetBrains/ideavim     6120           7              4
     6          3            1 JetBrains/intellij-community    12926           5              4
     7          0            0             JetBrains/kotlin    39402           7              0
    """.trimIndent(),
    """
       topicsRank watchersRank                         name watchers topicsCount promotionScore
     0          7            7     JetBrains/kotlinconf-app     2628           1             14
     1          6            6            JetBrains/ring-ui     2836           3             12
     2          5            5            JetBrains/Exposed     5688           4             10
     3          2            4      JetBrains/JetBrainsMono     6059           6              6
     4          4            2      JetBrains/kotlin-native     7101           5              6
     5          1            3            JetBrains/ideavim     6120           7              4
     6          3            1 JetBrains/intellij-community    12926           5              4
     7          0            0             JetBrains/kotlin    39402           7              0
    """.trimIndent(),
).map { output ->
    val text = output.split('\n').map { it.drop(2) }
    val titleLine = text.first()
    val titles = titleLine.split(" ").filter(String::isNotEmpty)
    val windows = titles.runningFold(0) { lastIndex, it -> titleLine.indexOf(it, startIndex = lastIndex) + it.length }
        .windowed(2)
    val lines = text.map {
        windows.map { (start, end) -> it.substring(start, end) } + listOf("\n")
    }
    lines.flatMap { line ->
        line.flatMap { item ->
            val firstNonWhitespace = item.indexOfFirst { !it.isWhitespace() }
            if (firstNonWhitespace > 0) {
                listOf(
                    AnnotatedString(item.substring(0, firstNonWhitespace)),
                    AnnotatedString(item.substring(firstNonWhitespace)),
                )
            } else {
                listOf(AnnotatedString(item))
            }
        }
    }
}

fun StoryboardBuilder.DataFrameExample() {
    require(SAMPLES.size == OUTPUT.size) { "Samples (${SAMPLES.size}) != Outputs (${OUTPUT.size})" }

    section("DataFrame") {
        KodeeScene(stateCount = 3) {
            Header()
            Body {
                RevealEach(frame.createChildTransition { it.toState() }) {
                    item { Text("$BULLET_1 DataFrame is an abstraction for working with structured data.") }
                    item { Text("$BULLET_1 It is hierarchical, interoperable, generic, and immutable.") }
                    item { Text("$BULLET_1 And now, thanks to a compiler-plugin, inferred.") }
                    // item { Text("$BULLET_1 The DataFrame compiler-plugin infers the schema at each step,") }
                    // item { Text("$BULLET_1 and generates synthetic extension properties for each schema.") }
                }
            }
        }

        KodeeScene {
            Header()

            // TODO could I hide some animation controls, to make them pausable and navigable?
            // When rendering the scene for preview, render the finished state and do not animate the sample.
            val displayType = LocalDisplayType.current
            var sampleIndex by remember {
                mutableIntStateOf(if (displayType == DisplayType.Story) 0 else SAMPLES.lastIndex)
            }
            val sampleTransition = updateTransition(sampleIndex)

            var outputIndex by remember { mutableIntStateOf(sampleIndex) }
            val outputTransition = updateTransition(outputIndex)

            StoryEffect(Unit) {
                while (true) {
                    delay(2.seconds)
                    if (sampleIndex == SAMPLES.lastIndex) {
                        // Delay a little longer on the last sample.
                        delay(3.seconds)
                        sampleIndex = 0
                    } else {
                        sampleIndex += 1
                        // Delay updating the output just a little
                        // until the sample is done updating.
                        delay(1200.milliseconds)
                    }

                    outputIndex = sampleIndex
                }
            }

            Box(Modifier.fillMaxSize()) {
                Body {
                    ProvideTextStyle(MaterialTheme.typography.code1) {
                        MagicText(sampleTransition.createChildTransition { SAMPLES[it].get().splitByTags() })
                    }
                }

                sampleTransition.AnimatedVisibility(
                    visible = { it != 0 },
                    enter = slideInVertically(tween(900, delayMillis = 900, easing = EaseIn)) { it },
                    exit = slideOutVertically(tween(900, easing = EaseOut)) { it },
                ) {
                    MacTerminal(modifier = Modifier.offset(y = 278.dp).fillMaxWidth()) {
                        ProvideTextStyle(MaterialTheme.typography.code2) {
                            MagicText(outputTransition.createChildTransition { OUTPUT[it] })
                        }
                    }
                }
            }
        }
    }
}
