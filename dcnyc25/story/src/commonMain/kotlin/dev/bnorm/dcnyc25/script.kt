package dev.bnorm.dcnyc25

import dev.bnorm.storyboard.easel.StoryState
import dev.bnorm.storyboard.easel.assist.Caption

// TODO name it teleprompter?
fun ScriptCaption(state: StoryState): Caption = Caption {
    """
        Hello everybody, my name is Brian Norman
        and I work for JetBrains on the Kotlin Compiler.
        And that introduction probably makes you wonder:
        what is a compiler developer,
        doing at an Android conference,
        talking about Compose.
        That's because this talk is less about my work on Kotlin 
        and more about a fun personal project.
        
        See, a little over a year ago, I was preparing a talk for KotlinConf.
        I was starting to prototype some things in Keynote and the whole experience was just absolutely frustrating.
        I was constantly copying code from IntelliJ to get the styling to look how I wanted,
          and then constantly duplicating slides to get the animation that I wanted.
        My developer brain was freaking out with how much duplication there was.
        
        So, I tried something.
        
        [advance]
        
        I was curious if I could build a presentation with Compose for desktop.
        Seemed reasonable enough, all the technology was there,
        I just needed to figure out a decent way to put it together.
        In fact I even managed to get a little bit of a prototype working.
        
        [advance]
        
        But, um, Hadi had other ideas.
        
        [advance]
        
        Well...
        
        [advance]
        
        I did it anyways.
        
        [advance]
        
        And created something I'm calling Storyboard.
    """.trimIndent()
}
