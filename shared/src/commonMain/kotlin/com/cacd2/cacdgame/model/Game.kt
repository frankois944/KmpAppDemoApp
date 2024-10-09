package com.cacd2.cacdgame.model

/**
 * Created by francois.dabonot@cacd2.fr on 14/06/2023.
 */
data class Game(
    val category: GameChoice,
    val questions: List<QuestionChoice>,
    val difficulty: Difficulty
)
