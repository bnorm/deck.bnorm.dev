package dev.bnorm.kc25.sections.intro

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bnorm.kc25.template.HeaderScaffold
import dev.bnorm.kc25.template.INTELLIJ_DARK_CODE_STYLE
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
    val cls by tag("")
    val body by tag("")
    val gen by tag("")
    val genb by tag("")
    val descb by tag("descriptor body")
    val serb by tag("serialize() body")
    val deserb by tag("deserialize body")

    val base = """
        @Serializable
        data class Project(val name: String, val language: String)${cls} {${body}
          companion object {
            fun serializer(): KSerializer<Project> = GENERATED
          }
        
          ${gen}private object GENERATED : KSerializer<Project>${genb} {
            override val descriptor: SerialDescriptor =${descb}
              buildClassSerialDescriptor("org.example.Project") {
                element<String>("name")
                element<String>("language")
              }${descb}
        
            override fun serialize(encoder: Encoder, value: Project) {${serb}
              encoder.encodeStructure(descriptor) {
                encodeStringElement(descriptor, 0, value.name)
                encodeStringElement(descriptor, 1, value.language)
              }
            ${serb}}
        
            override fun deserialize(decoder: Decoder): Project {${deserb}
              var name: String? = null
              var language: String? = null
              decoder.decodeStructure(descriptor) {
                while (true) {
                  when (val index = decodeElementIndex(descriptor)) {
                    DECODE_DONE -> break
                    0 -> name = decodeStringElement(descriptor, index = 0)
                    1 -> language = decodeStringElement(descriptor, index = 1)
                    else -> throw SerializationException("Unexpected index ${'$'}index")
                  }
                }
                if (name == null || language == null)
                  throw SerializationException("Missing fields")
              }
              return Project(name!!, language!!)
            ${deserb}}
          }${genb}${gen}
        ${body}}${cls}
    """.trimIndent().toCodeSample(INTELLIJ_DARK_CODE_STYLE)

    base.hide(cls, genb).collapse(descb, serb, deserb)
        .then { reveal(cls).focus(body) }
        .then { reveal(genb).focus(gen) }
        .then { unfocus().attach(5.seconds) }
}

fun StoryboardBuilder.SerializationExample() {
    section("Serialization") {
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
