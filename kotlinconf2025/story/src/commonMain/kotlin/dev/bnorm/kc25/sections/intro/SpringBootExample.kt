package dev.bnorm.kc25.sections.intro

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bnorm.kc25.template.HeaderScaffold
import dev.bnorm.kc25.template.code.CodeSample
import dev.bnorm.kc25.template.code.buildCodeSamples
import dev.bnorm.kc25.template.code1
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.easel.template.section
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.text.splitByTags
import kotlin.time.Duration.Companion.seconds

private val SAMPLES = buildCodeSamples {
    val f by tag("focus")
    val open by tag("open modifiers")

    val base = """
        ${f}@SpringBootApplication${f}
        ${f}${open}open ${open}${f}class Application {
          ${f}@Bean${f}
          ${f}${open}open ${open}${f}fun greeting(): String = "welcome"
        }
        
        ${f}@Controller${f}
        ${f}${open}open ${open}${f}class WelcomeController(val greeting: String) {

          ${f}@GetMapping("/")${f}
          ${f}${open}open ${open}${f}fun welcome(): String = greeting
        }
    """.trimIndent().toCodeSample()

    base.hide(open)
        .then { focus(f) }
        .then { reveal(open) }
        .then { unfocus().attach(5.seconds) }
}

fun StoryboardBuilder.SpringBoot(sink: MutableList<CodeSample>) {
    sink.addAll(SAMPLES)
    section("Spring Boot") {
        scene(
            enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
            exitTransition = SceneExit(alignment = Alignment.CenterEnd),
        ) {
            HeaderScaffold { padding ->
                val index by animateSampleIndex(samples = SAMPLES)

                Box(Modifier.padding(padding)) {
                    ProvideTextStyle(MaterialTheme.typography.code1) {
                        MagicText(SAMPLES[index].string.splitByTags())
                    }
                }
            }
        }
    }
}
