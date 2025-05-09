package dev.bnorm.kc25.sections.stages

import dev.bnorm.kc25.template.SectionTitle
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.section
import kotlinx.collections.immutable.mutate
import kotlinx.collections.immutable.persistentMapOf

fun StoryboardBuilder.Architecture() {
    section("Architecture") {
        val empty = persistentMapOf<CompilerStage, CompilerStageState>()
        val visible = persistentMapOf(
            CompilerStage.Parse to CompilerStageState(),
            CompilerStage.Resolve to CompilerStageState(),
            CompilerStage.Analyze to CompilerStageState(),
            CompilerStage.Transform to CompilerStageState(),
            CompilerStage.Generate to CompilerStageState(),
        )
        val content = persistentMapOf(
            CompilerStage.Parse to CompilerStageState(content = true),
            CompilerStage.Resolve to CompilerStageState(content = true),
            CompilerStage.Analyze to CompilerStageState(content = true),
            CompilerStage.Transform to CompilerStageState(content = true),
            CompilerStage.Generate to CompilerStageState(content = true),
        )

        val parseDetail = empty.put(CompilerStage.Parse, CompilerStageState(content = true, focused = true))
        val resolveDetail = empty.put(CompilerStage.Resolve, CompilerStageState(content = true, focused = true))
        val analyzeDetail = empty.put(CompilerStage.Analyze, CompilerStageState(content = true, focused = true))
        val transformDetail = empty.put(CompilerStage.Transform, CompilerStageState(content = true, focused = true))
        val generateDetail = empty.put(CompilerStage.Generate, CompilerStageState(content = true, focused = true))

        val parseContent = visible.put(CompilerStage.Parse, CompilerStageState(content = true))
        val resolveContent = parseContent.put(CompilerStage.Resolve, CompilerStageState(content = true))
        val analyzeContent = resolveContent.put(CompilerStage.Analyze, CompilerStageState(content = true))
        val transformContent = analyzeContent.put(CompilerStage.Transform, CompilerStageState(content = true))
        val generateContent = transformContent.put(CompilerStage.Generate, CompilerStageState(content = true))

        val parseProgress = parseContent.put(CompilerStage.Parse, CompilerStageState(content = true, focused = true))
        val resolveProgress = resolveContent.put(CompilerStage.Resolve, CompilerStageState(content = true, focused = true))
        val analyzeProgress = analyzeContent.put(CompilerStage.Analyze, CompilerStageState(content = true, focused = true))
        val transformProgress = transformContent.put(CompilerStage.Transform, CompilerStageState(content = true, focused = true))
        val generateProgress = generateContent.put(CompilerStage.Generate, CompilerStageState(content = true, focused = true))

        val frontend = analyzeContent.mutate { builder ->
            builder.put(CompilerStage.Parse, CompilerStageState(content = true, focused = true))
            builder.put(CompilerStage.Resolve, CompilerStageState(content = true, focused = true))
            builder.put(CompilerStage.Analyze, CompilerStageState(content = true, focused = true))
        }
        val backend = analyzeContent.mutate { builder ->
            builder.put(CompilerStage.Transform, CompilerStageState(focused = true))
            builder.put(CompilerStage.Generate, CompilerStageState(focused = true))
        }

        SectionTitle()
        StageTimeline(listOf(visible, parseProgress), start = visible, end = parseDetail)
        StageParse()
        StageTimeline(listOf(parseProgress, resolveProgress), start = parseDetail, end = resolveDetail)
        StageResolve()
        StageTimeline(listOf(resolveProgress, analyzeProgress), start = resolveDetail, end = analyzeDetail)
        StageAnalyze()
        StageTimeline(listOf(analyzeProgress, frontend, backend, transformProgress), start = analyzeDetail, end = transformDetail)
        StageTransform()
        StageTimeline(listOf(transformProgress, generateProgress), start = transformDetail, end = generateDetail)
        StageGenerate()
        StageTimeline(listOf(generateProgress, content), start = generateDetail, end = content)
    }
}
