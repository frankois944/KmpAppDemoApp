//
//  QuestionListingRowView.swift
//  CACDGAME
//
//  Created by frankois on 13/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import Shared

struct QuestionListingRowView: View {

    let question: QuestionChoice

    var body: some View {
        HStack(alignment: .center) {
            if let userSelectedAnswer =  question.userSelectedAnswer {
                if userSelectedAnswer.isCorrect {
                    R.image.check_good.image.foregroundColor(R.color.primary.color)
                        .padding(.leading, 16)
                } else {
                    R.image.check_bad.image.foregroundColor(R.color.error.color)
                        .padding(.leading, 16)
                }
            } else {
                R.image.check_good.image.foregroundColor(R.color.primary.color)
                    .opacity(0)
                    .padding(.leading, 0)
                    .frame(height: 24)
            }
            Text(question.content)
                .font(R.font.robotoRegular.font(size: 16))
                .foregroundColor(R.color.onSurface.color)
                .kerning(0.15)
                .multilineTextAlignment(.leading)
                .lineLimit(2)
                .padding(.leading, 8)
                .padding(.trailing, 10)
            Spacer()
            R.image.chevron_right.image.foregroundColor(R.color.onSurface.color).padding(.trailing, 16)
        }
    }
}

#Preview {
    VStack {
        QuestionListingRowView(question: QuestionChoice.companion.dummy)
        Divider()
        QuestionListingRowView(question: QuestionChoice(
            id: "1",
            content: "une question une question ",
            detail: nil,
            game: .design,
            answers: [],
            userSelectedAnswer: AnswerChoice(id: "1", content: "1", isCorrect: false),
            difficulty: .easy,
            lang: .english,
            illustration: nil,
            responseTime: 0
        ))
        Divider()
        QuestionListingRowView(question: QuestionChoice(
            id: "1",
            content: "une question une question une question une question une question une question une question",
            detail: nil,
            game: .design,
            answers: [],
            userSelectedAnswer: AnswerChoice(id: "1", content: "1", isCorrect: true),
            difficulty: .easy,
            lang: .english,
            illustration: nil,
            responseTime: 0
        ))
        Divider()
    }
    .previewLayout(.sizeThatFits)
    .environmentObject(AppContext())
}
