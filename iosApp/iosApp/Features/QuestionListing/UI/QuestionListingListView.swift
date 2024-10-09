//
//  QuestionListingListView.swift
//  CACDGAME
//
//  Created by François Dabonot on 26/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import Shared

struct QuestionListingListView: View {

    let questions: [QuestionChoice]
    let withBackground: Bool

    var body: some View {
        ScrollView {
            ZStack {
                LazyVStack(alignment: .leading) {
                    ForEach(Array(questions.enumerated()), id: \.offset) { index, row in
                        NavigationLink(value: Router.Route.answerDefinition(model: .init(question: row)).value) {
                            QuestionListingRowView(question: row)
                        }
                        .if(index == 0, true: {
                            $0.padding(.top, 8)
                        })
                        Divider().padding(.leading, row.userSelectedAnswer == nil ? 40 : 60)
                    }
                }
            }
            // .if(withBackground, true: {
            /*$0.background(content: {
             EmptyView()
             .frame(maxWidth: .infinity, maxHeight: .infinity)
             .modifier(CommonCardBackground(padding: 0))
             })
             .frame(maxWidth: .infinity, maxHeight: .infinity)*/
            // })
        }
    }
}

#Preview {
    QuestionListingListView(questions: [
        QuestionChoice(
            id: "1",
            content: "Une question",
            detail: nil,
            game: .design,
            answers: [],
            userSelectedAnswer: AnswerChoice(id: "1", content: "1", isCorrect: false),
            difficulty: .easy,
            lang: .english,
            illustration: nil,
            responseTime: 0
        ),
        QuestionChoice(
            id: "2",
            content: "Une autre question",
            detail: nil,
            game: .design,
            answers: [],
            userSelectedAnswer: AnswerChoice(id: "1", content: "1", isCorrect: true),
            difficulty: .easy,
            lang: .english,
            illustration: nil,
            responseTime: 0
        ),
        QuestionChoice(
            id: "3",
            content: "Une autre question",
            detail: nil,
            game: .design,
            answers: [],
            userSelectedAnswer: nil,
            difficulty: .easy,
            lang: .english,
            illustration: nil,
            responseTime: 0
        )
    ], withBackground: true)
}
