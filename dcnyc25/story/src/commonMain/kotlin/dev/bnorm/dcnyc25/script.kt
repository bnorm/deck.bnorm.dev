package dev.bnorm.dcnyc25

import dev.bnorm.storyboard.easel.StoryState
import dev.bnorm.storyboard.easel.assist.Caption

// TODO name it teleprompter?
fun ScriptCaption(state: StoryState): Caption = Caption {
    """
        Hello everybody, my name is Brian Norman
        and I work for JetBrains on the Kotlin Compiler.
        I'm here today to talk to you about... Compose?
        Well, I guess more what you can do with Compose, that's a little non-standard.
        
        See, a little over a year ago, I was preparing a talk.
        I was getting really frustrated with Keynote and all the duplication I was doing,
        and I had this crazy idea to build my presentation with Compose.
        
        [advance]
        
        In fact I even managed to get a little bit of a prototype working.
        I was really excited about the potential.
        
        [advance]
        
        But, um, Hadi had other opinions.
        
        [advance]
        
        Well...
        
        [advance]
        
        I did it anyways.
        I presented that talk using slides written in Compose and it went really well.
        In fact, I did it again a couple weeks ago, and I'm doing it again now.

        All of the slides you are about to see are written in Compose using a library...
        [advance]
        I'm calling Storyboard.
        
        (now this isn't to be confused with Storytale, the JetBrains' Compose gallary generator)
        (nor is it to be confused with Storybook, the JavaScript UI test framework)
        (nor is it to be confused with Storyboard, the XCode tool for iOS development)
        (apparently I'm bad at picking a name...)
        
        But Storyboard is a little Compose framework for creating presentations.
        It has a small DSL for defining slides and manages state within those slides and transitions between them.
        
        [advance]
        
        But that's really about it.
        There isn't much to the core of Storyboard.
    """.trimIndent()
}
