//
//  GamePlayBoardContentView.swift
//  CACDGAME
//
//  Created by frankois on 21/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import Shared

struct GamePlayBoardContentView: View {

    @EnvironmentObject var appContext: AppContext

    let questions: [QuestionChoice]
    let onNextQuestion: () -> Void
    @Binding var currentQuestionIndex: Int
    @Binding var selectedAnswerChoice: AnswerChoice?
    @Binding var showDefinition: QuestionChoice?

    init(questions: [QuestionChoice],
         currentQuestionIndex: Binding<Int>,
         selectedAnswerChoice: Binding<AnswerChoice?>,
         onNextQuestion: @escaping () -> Void,
         showDefinition: Binding<QuestionChoice?>) {
        self.questions = questions
        self._currentQuestionIndex = currentQuestionIndex
        self._selectedAnswerChoice = selectedAnswerChoice
        self.onNextQuestion = onNextQuestion
        self._showDefinition = showDefinition
    }

    var currentQuestion: QuestionChoice {
        questions[currentQuestionIndex]
    }

    var hasInCorrectAnswer: Bool {
        if let selectedAnswerChoice = selectedAnswerChoice {
            return selectedAnswerChoice.isCorrect == false
        }
        return false
    }

    var body: some View {
        GamePlayProgressView(currentQuestion: currentQuestionIndex,
                             totalQuestion: questions.count)
        Spacer()
        Text(currentQuestion.content)
            .foregroundColor(R.color.onSurface.color)
            .font(R.font.poppinsExtraBold.font(size: 20))
            .multilineTextAlignment(.center)
            .padding(.horizontal)
            .lineLimit(4)
            .minimumScaleFactor(0.75)
            .fixedSize(horizontal: false, vertical: true)
        Spacer()
        GamePlayAnswerSelectorView(answers: currentQuestion.answers,
                                   selectedAnswerChoice: $selectedAnswerChoice)
            .padding()
        Spacer()
        PrimaryButton(R.string.localizable.see_definition.string) {
            showDefinition = currentQuestion
        }.opacity(hasInCorrectAnswer ? 1 : 0)
        Spacer()
        NavigationFooterView {
            self.onNextQuestion()
        }.opacity(hasInCorrectAnswer ? 1 : 0)
        Spacer()
    }
}

#Preview("Light") {
    GamePlayBoardContentView(
        questions: [
            QuestionChoice.companion.dummy,
            QuestionChoice.companion.dummy
        ],
        currentQuestionIndex: .constant(0),
        selectedAnswerChoice: .constant(nil),
        onNextQuestion: { },
        showDefinition: .constant(nil))
        .environmentObject(AppContext(username: "DEBUG"))
        .environmentObject(Router())
}

#Preview("Dark") {
    GamePlayBoardContentView(
        questions: [
            QuestionChoice.companion.dummy,
            QuestionChoice.companion.dummy
        ],
        currentQuestionIndex: .constant(1),
        selectedAnswerChoice: .constant(QuestionChoice.companion.dummy.answers[0]),
        onNextQuestion: { },
        showDefinition: .constant(nil))
        .environmentObject(AppContext(username: "DEBUG"))
        .environmentObject(Router())
        .preferredColorScheme(.dark)
}
