package dev.bnorm.kc25.sections.plugin

import dev.bnorm.kc25.template.CompilerStage
import dev.bnorm.kc25.template.SectionTitle
import dev.bnorm.kc25.template.StageDetail
import dev.bnorm.kc25.template.StageTimeline
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
    StageDetail(CompilerStage.Parse)
    StageTimeline(CompilerStage.Parse)
    StageDetail(CompilerStage.Resolve)
    StageTimeline(CompilerStage.Resolve)
    StageDetail(CompilerStage.Analyse)
    StageTimeline(CompilerStage.Analyse)
    StageDetail(CompilerStage.Transform)
    StageTimeline(CompilerStage.Transform)
    StageDetail(CompilerStage.Generate)
    StageTimeline(CompilerStage.Generate)
}
