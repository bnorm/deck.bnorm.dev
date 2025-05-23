package dev.bnorm.kc25.sections.intro

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.bnorm.kc25.components.BottomPanel
import dev.bnorm.kc25.template.HeaderScaffold
import dev.bnorm.kc25.template.code.CodeSample
import dev.bnorm.kc25.template.code.buildCodeSamples
import dev.bnorm.kc25.template.code1
import dev.bnorm.kc25.template.code2
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.*
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.text.splitByTags
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
        .then { unfocus().attach(5.seconds) }
}

// TODO how to make these lazy?
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

fun StoryboardBuilder.DataFrameExample(sink: MutableList<CodeSample>) {
    require(SAMPLES.size == OUTPUT.size) { "Samples (${SAMPLES.size}) != Outputs (${OUTPUT.size})" }
    sink.addAll(SAMPLES)

    section("DataFrame") {
        scene(
            enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
            exitTransition = SceneExit(alignment = Alignment.CenterEnd),
        ) {
            HeaderScaffold { padding ->
                val sampleIndex by animateSampleIndex(samples = SAMPLES)

                var outputIndex by remember { mutableIntStateOf(sampleIndex) }

                if (sampleIndex == 0) {
                    outputIndex = sampleIndex
                } else {
                    StoryEffect(sampleIndex) {
                        if (sampleIndex != 0) {
                            delay(1200.milliseconds)
                            outputIndex = sampleIndex
                        }
                    }
                }

                val sampleTransition = updateTransition(sampleIndex)
                val outputTransition = updateTransition(outputIndex)

                Box(Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier.padding(padding),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        ProvideTextStyle(MaterialTheme.typography.code1) {
                            MagicText(sampleTransition.createChildTransition { SAMPLES[it].string.splitByTags() })
                        }
                    }

                    // TODO different border color?
                    BottomPanel(
                        show = sampleTransition.createChildTransition { it != 0 },
                        enterSpec = tween(900, delayMillis = 900, easing = EaseIn),
                        exitSpec = tween(900, easing = EaseOut),
                        modifier = Modifier.offset(y = 38.dp)
                    ) {
                        Box(Modifier.padding(start = 32.dp, top = 16.dp, end = 32.dp)) {
                            ProvideTextStyle(MaterialTheme.typography.code2) {
                                MagicText(outputTransition.createChildTransition { OUTPUT[it] })
                            }
                        }
                    }
                }
            }
        }
    }
}
