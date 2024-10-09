//
//  GamePlayAnswerSelectorView.swift
//  CACDGAME
//
//  Created by François Dabonot on 19/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import Shared

struct GamePlayAnswerSelectorView: View {

    let answers: [AnswerChoice]
    @Binding var selectedAnswerChoice: AnswerChoice?

    var body: some View {
        VStack(spacing: 20) {
            HStack(spacing: 20) {
                GamePlayAnswerItemView(answser: answers[0],
                                       selectedAnswerChoice: $selectedAnswerChoice)
                    .aspectRatio(1, contentMode: .fit)
                GamePlayAnswerItemView(answser: answers[1],
                                       selectedAnswerChoice: $selectedAnswerChoice)
                    .aspectRatio(1, contentMode: .fit)
            }
            HStack(spacing: 20) {
                GamePlayAnswerItemView(answser: answers[2],
                                       selectedAnswerChoice: $selectedAnswerChoice)
                    .aspectRatio(1, contentMode: .fit)
                GamePlayAnswerItemView(answser: answers[3],
                                       selectedAnswerChoice: $selectedAnswerChoice)
                    .aspectRatio(1, contentMode: .fit)
            }
        }
    }
}

#Preview {
    GamePlayAnswerSelectorView(
        answers: QuestionChoice.companion.dummy.answers,
        selectedAnswerChoice: .constant(QuestionChoice.companion.dummy.answers[0])
    )
    .padding()
    .preferredColorScheme(.light)
    .previewLayout(PreviewLayout.sizeThatFits)
    .environmentObject(AppContext())
}

#Preview {
    GamePlayAnswerSelectorView(
        answers: QuestionChoice.companion.dummy.answers,
        selectedAnswerChoice: .constant(QuestionChoice.companion.dummy.answers[0])
    )
    .padding()
    .preferredColorScheme(.dark)
    .previewLayout(PreviewLayout.sizeThatFits)
    .environmentObject(AppContext())
}
