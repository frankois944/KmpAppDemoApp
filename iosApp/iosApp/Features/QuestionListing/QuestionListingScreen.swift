//
//  QuestionListingScreen.swift
//  CACDGAME
//
//  Created by François Dabonot on 06/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import Shared

struct QuestionListingScreen: View {

    static let route = QuestionListingModel.self

    let model: QuestionListingModel

    @State var questions: [QuestionChoice]?

    init(model: QuestionListingModel, questions: [QuestionChoice]? = nil) {
        self.model = model
        self.questions = questions
    }

    var body: some View {
        QuestionListingListView(questions: questions ?? [], withBackground: false)
            .task {
                let content = try? await Database.companion.data.getHistory(historyId: Int64(model.historyId))
                if let isSuccess = model.isSuccess {
                    questions = content?.data.filter {
                        $0.userSelectedAnswer?.isCorrect == isSuccess
                    }
                } else {
                    questions = content?.data ?? []
                }
            }
            .padding(.top, 10)
            .modifier(CommonCardBackground())
    }
}

#Preview {
    QuestionListingScreen(model: .init(historyId: 1), questions:
                            HistoryData.companion.dummy.data
    )
    .environmentObject(AppContext())
    .environmentObject(Router())
}
