package dev.bnorm.kc25.sections.stages

import dev.bnorm.kc25.template.SectionTitle
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.section

fun StoryboardBuilder.Architecture() {
    section("Architecture") {
        SectionTitle()
        StageTimeline(null, CompilerStage.Parse)
        StageParse()
        StageTimeline(CompilerStage.Parse, CompilerStage.Resolve)
        StageResolve()
        StageTimeline(CompilerStage.Resolve, CompilerStage.Analyze)
        StageAnalyze()
        StageTimeline(CompilerStage.Analyze, CompilerStage.Transform)
        StageTransform()
        StageTimeline(CompilerStage.Transform, CompilerStage.Generate)
        StageGenerate()
        StageTimeline(CompilerStage.Generate, null)
    }
}
