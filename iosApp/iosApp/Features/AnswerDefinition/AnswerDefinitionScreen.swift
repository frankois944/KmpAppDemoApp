//
//  AnswerDefinitionScreen.swift
//  CACDGAME
//
//  Created by François Dabonot on 06/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import Shared

struct AnswerDefinitionScreen: View {

    static let route = AnswerDefinitionModel.self

    @Environment(\.dismiss) private var dismiss
    @EnvironmentObject var router: Router
    let model: AnswerDefinitionModel

    var body: some View {
        AnswerDefinitionContentView(question: model.question, canGoToNextQuestion: model.canGoToNextQuestion)
            .overlay {
                if model.canGoToNextQuestion == true {
                    VStack {
                        Spacer()
                        PrimaryButton(R.string.localizable.next_question.string) {
                            dismiss()
                        }
                        .padding(.bottom, 24)
                    }
                }
            }
            .modifier(CommonCardBackground())
    }
}

#Preview {
    AnswerDefinitionScreen(model: .init(question: QuestionChoice.companion.dummy, canGoToNextQuestion: true))
        .environmentObject(AppContext())
        .environmentObject(Router())
}

#Preview {
    AnswerDefinitionScreen(model: .init(question: QuestionChoice.companion.dummy, canGoToNextQuestion: false))
        .environmentObject(AppContext())
        .environmentObject(Router())
}
