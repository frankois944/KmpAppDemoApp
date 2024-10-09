//
//  GamePlayAnswerItemView.swift
//  CACDGAME
//
//  Created by François Dabonot on 19/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import Shared

struct GamePlayAnswerItemView: View {

    @EnvironmentObject var appContext: AppContext
    @Binding var selectedAnswerChoice: AnswerChoice?
    let answser: AnswerChoice
    let isSelected: Bool
    let forceSelectAnswer: Bool

    init(answser: AnswerChoice,
         selectedAnswerChoice: Binding<AnswerChoice?>) {
        self.answser = answser
        self._selectedAnswerChoice = selectedAnswerChoice
        self.isSelected = selectedAnswerChoice.wrappedValue?.id == answser.id
        if let selectedAnswerChoice = selectedAnswerChoice.wrappedValue,
           !selectedAnswerChoice.isCorrect,
           answser.isCorrect {
            self.forceSelectAnswer = true
        } else {
            self.forceSelectAnswer = false
        }
    }

    var background: Color {
        if isSelected || forceSelectAnswer {
            if answser.isCorrect {
                return R.color.primary.color.opacity(0.12)
            } else {
                return R.color.error.color.opacity(0.12)
            }
        } else {
            return R.color.unselectedContentColor.color
        }
    }

    var border: Color {
        if isSelected || forceSelectAnswer {
            if answser.isCorrect {
                return R.color.primary.color
            } else {
                return R.color.error.color
            }
        } else {
            return R.color.unselectedBorderContentColor.color
        }
    }

    var textStyle: Font {
        if isSelected || forceSelectAnswer {
            return R.font.poppinsBold.font(size: 14)
        } else {
            return R.font.poppinsMedium.font(size: 14)
        }
    }

    var textColor: Color {
        if isSelected || forceSelectAnswer {
            if answser.isCorrect {
                return R.color.primary.color
            } else {
                return R.color.error.color
            }
        } else {
            return R.color.onSurface.color
        }
    }

    var body: some View {

        Text(answser.content)
            .padding()
            .multilineTextAlignment(.center)
            .minimumScaleFactor(0.75)
            .lineLimit(4)
            .foregroundColor(textColor)
            .font(textStyle)
            .frame(maxWidth: .infinity,
                   maxHeight: .infinity,
                   alignment: .center)
            .aspectRatio(1, contentMode: .fit)
            .overlay {
                ZStack {
                    if answser.isCorrect {
                        R.image.check_good.image
                            .foregroundColor(R.color.primary.color)
                    } else {
                        R.image.check_bad.image
                            .foregroundColor(R.color.error.color)
                    }
                }
                .padding(.top, 8)
                .padding(.trailing, 8)
                .opacity(isSelected || forceSelectAnswer ? 1 : 0)
                .frame(maxWidth: .infinity,
                       maxHeight: .infinity,
                       alignment: .topTrailing)
            }
            .background(
                RoundedCorners(background: background,
                               border: border,
                               topLeft: 12,
                               topRight: 4,
                               bottomLeft: 4,
                               bottomRight: 12)
            )
            .onTapGesture {
                guard selectedAnswerChoice == nil else { return }
                selectedAnswerChoice = answser
            }
    }
}

#Preview("Neutre") {
    GamePlayAnswerItemView(answser: QuestionChoice.companion.dummy.answers.first!,
                           selectedAnswerChoice: .constant(nil))
        .previewLayout(PreviewLayout.sizeThatFits)
        .aspectRatio(1, contentMode: .fit)
        .environmentObject(AppContext(username: "DEBUG"))
}

#Preview("KO") {
    GamePlayAnswerItemView(answser: QuestionChoice.companion.dummy.answers.first!,
                           selectedAnswerChoice: .constant(QuestionChoice.companion.dummy.answers.first!))
        .previewLayout(PreviewLayout.sizeThatFits)
        .aspectRatio(1, contentMode: .fit)
        .environmentObject(AppContext(username: "DEBUG"))
}

let current = QuestionChoice.companion.dummy.answers[1]
let selected = QuestionChoice.companion.dummy.answers[1]

#Preview("OK") {
    GamePlayAnswerItemView(answser: current,
                           selectedAnswerChoice: .constant(selected))
        .previewLayout(PreviewLayout.sizeThatFits)
        .aspectRatio(1, contentMode: .fit)
        .environmentObject(AppContext(username: "DEBUG"))
}
