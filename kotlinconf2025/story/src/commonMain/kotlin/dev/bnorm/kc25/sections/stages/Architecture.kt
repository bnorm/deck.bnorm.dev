package dev.bnorm.kc25.sections.stages

import dev.bnorm.kc25.template.SectionTitle
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.section

// TODO actually fill in details about stages
fun StoryboardBuilder.Architecture() {
    section("Architecture") {
        SectionTitle()
        CompilerStages()
    }
}

private fun StoryboardBuilder.CompilerStages() {
    StageTimeline(null)
    StageParse()
    StageTimeline(CompilerStage.Parse)
    StageResolve()
    StageTimeline(CompilerStage.Resolve)
    StageAnalyze()
    StageTimeline(CompilerStage.Analyze)
    // TODO talk about frontend vs backend?
    StageTransform()
    StageTimeline(CompilerStage.Transform)
    StageGenerate()
    StageTimeline(CompilerStage.Generate)
}
