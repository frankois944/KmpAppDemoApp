//
//  AnswerDefinitionModel.swift
//  CACDGAME
//
//  Created by François Dabonot on 25/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import Shared

struct AnswerDefinitionModel: Hashable {

    let question: QuestionChoice
    let canGoToNextQuestion: Bool

    init(question: QuestionChoice, canGoToNextQuestion: Bool = false) {
        self.question = question
        self.canGoToNextQuestion = canGoToNextQuestion
    }
}
