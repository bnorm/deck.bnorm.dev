package dev.bnorm.kc25.sections.stages

import dev.bnorm.kc25.template.SectionTitle
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.section

fun StoryboardBuilder.Architecture() {
    section("Architecture") {
        SectionTitle()

        // TODO the stages should be more front and center
        //  - center align them?
        //  - push them to the outside?
        //  - other slides can still have them as a header
        //  - can move from center to the top

        // TODO are there other visuals we want to add here?

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
}
