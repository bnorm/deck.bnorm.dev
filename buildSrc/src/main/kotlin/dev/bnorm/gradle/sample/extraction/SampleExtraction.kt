package dev.bnorm.gradle.sample.extraction

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

open class SampleExtraction : DefaultTask() {
    companion object {
        private val START = "//\\s*@sample-start:(?<name>[^\n]+)".toRegex()
        private val END = "//\\s*@sample-end".toRegex()
    }

    private sealed class Token {
        abstract val index: Int

        class Start(val name: String, override val index: Int) : Token()
        class End(override val index: Int) : Token()
    }

    class Sample(
        val file: File,
        val name: String,
        val text: String,
    )

    @get:InputDirectory
    val input = project.objects.directoryProperty()

    @get:OutputDirectory
    val output = project.objects.directoryProperty()
        .convention(project.layout.buildDirectory.dir("generated/samples"))

    @TaskAction
    fun perform() {
        val samples = findSamples()
            .groupBy { it.file to it.name }
            .mapValues { (key, samples) -> Sample(key.first, key.second, samples.joinToString("\n\n") { it.text }) }
            .values

        val inputDirectory = input.asFile.get()
        val outputDirectory = output.asFile.get()
        outputDirectory.deleteRecursively()

        for (sample in samples) {
            val parentFile = outputDirectory.resolve(sample.file.parentFile.relativeTo(inputDirectory))
            parentFile.mkdirs()

            val output = parentFile.resolve("${sample.file.name}@${sample.name}")
            output.writeText(sample.text)
        }
    }

    private fun findSamples(): List<Sample> = buildList {
        for (file in input.asFileTree) {
            val text = file.readText()
            val tokens = findAllTokens(text)

            val starts = mutableListOf<Token.Start>()
            for (token in tokens) {
                when (token) {
                    is Token.Start -> starts.add(token)
                    is Token.End -> {
                        val start = starts.removeLastOrNull() ?: continue // TODO error?

                        val sampleText = text.substring(startIndex = start.index, endIndex = token.index)
                            .lines()
                            .filter { !START.containsMatchIn(it) && !END.containsMatchIn(it) }
                            .joinToString("\n") { it.trimEnd() }
                            .trimIndent()

                        add(Sample(file, start.name, sampleText))
                    }
                }
            }
        }
    }

    private fun findAllTokens(text: String): Sequence<Token> {
        val startMatches = START.findAll(text)
            .map { Token.Start(it.groupValues[1], it.range.endInclusive + 1) }

        val endMatches = END.findAll(text)
            .map { Token.End(it.range.start) }

        return (startMatches + endMatches).sortedBy { it.index }
    }
}
