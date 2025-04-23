package dev.bnorm.kc25.sections.stages

import dev.bnorm.kc25.template.SectionTitle
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.section

fun StoryboardBuilder.Architecture() {
    section("Architecture") {
        SectionTitle()

        // TODO the stages should be more front and center
        //  - push them to the outside?
        //  - can move from center to the top

        // TODO are there other visuals we want to add here?

        StageTimeline(null, CompilerStage.Parse)
        StageParse()
        StageTimeline(CompilerStage.Parse, CompilerStage.Resolve)
        StageResolve()
        StageTimeline(CompilerStage.Resolve, CompilerStage.Analyze)
        StageAnalyze()
        StageTimeline(CompilerStage.Analyze, CompilerStage.Transform)
        // TODO talk about frontend vs backend
        //  - put boxes around the frontend and the backend
        StageTransform()
        StageTimeline(CompilerStage.Transform, CompilerStage.Generate)
        StageGenerate()
        StageTimeline(CompilerStage.Generate, null)
    }
}
