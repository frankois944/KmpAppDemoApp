//
//  GamePlayBoardView.swift
//  CACDGAME
//
//  Created by François Dabonot on 19/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import Shared
import Combine

struct GamePlayBoardView: View {

    let questions: [QuestionChoice]
    @EnvironmentObject var router: Router
    @EnvironmentObject var appContext: AppContext
    @Binding var showResult: [QuestionChoice]?
    @Binding var cancelGame: Bool
    @Binding var definitionToDisplay: QuestionChoice?
    @Binding var currentQuestionIndex: Int
    @State var displayResultBanner: Bool?
    @State var uiState: GamePlayUIState
    @State var selectedAnswerChoice: AnswerChoice?
    @State var currentTimeoutProgression: Double = 0
    @State var timeoutDisplayed: Bool?
    @State var cTime = Date.now.timeIntervalSince1970
    @State var currentTick: TimeInterval = 0
    @State private var startTimer = Date.now.timeIntervalSince1970
    @State private var timer = Timer.publish(every: 0.050, on: .main, in: .common).autoconnect()
    @State var displayDefinition = false

    init(questions: [QuestionChoice],
         showResult: Binding<[QuestionChoice]?>,
         cancelGame: Binding<Bool>,
         showDefinition: Binding<QuestionChoice?>,
         currentQuestionIndex: Binding<Int>,
         currentState: GamePlayUIState = .inProgress) {
        self.questions = questions
        self._showResult = showResult
        self._cancelGame = cancelGame
        self._definitionToDisplay = showDefinition
        self._currentQuestionIndex = currentQuestionIndex
        self.uiState = currentState
    }

    private func goToNextQuestion() {
        if uiState == .timeout {
            let firstWrongAnswer = questions[currentQuestionIndex].answers.first { !$0.isCorrect }
            questions[currentQuestionIndex].userSelectedAnswer = firstWrongAnswer
            questions[currentQuestionIndex].responseTime = Int64(Date.now.timeIntervalSince1970 - cTime)
        }
        if questions[currentQuestionIndex].userSelectedAnswer != nil {
            if currentQuestionIndex + 1 >= questions.count {
                uiState = .ending
                showResult = questions
            } else {
                currentTick = 0
                timer = timer.upstream.autoconnect()
                currentQuestionIndex += 1
                selectedAnswerChoice = nil
                timeoutDisplayed = nil
                displayResultBanner = nil
                cTime = Date.now.timeIntervalSince1970
                uiState = .inProgress
            }
        }
    }

    private func onSelectAnswerChoice(selected: AnswerChoice?) {
        guard selectedAnswerChoice != nil, selected != nil else { return }
        selectedAnswerChoice = selected
        questions[currentQuestionIndex].responseTime = Int64(Date.now.timeIntervalSince1970 - cTime)
        questions[currentQuestionIndex].userSelectedAnswer = selected
        uiState = selected?.isCorrect == true ? .success : .failure
    }

    var body: some View {
        VStack {
            if uiState == .timeout {
                GamePlayTimeoutView(isDisplayed: $timeoutDisplayed)
                    .frame(maxWidth: .infinity)
            } else {
                GamePlayBoardContentView(
                    questions: questions,
                    currentQuestionIndex: $currentQuestionIndex,
                    selectedAnswerChoice: $selectedAnswerChoice,
                    onNextQuestion: {
                        goToNextQuestion()
                    },
                    showDefinition: $definitionToDisplay)
            }
        }
        .onChange(of: definitionToDisplay, perform: { newValue in
            displayDefinition = newValue != nil
        })
        .sheet(isPresented: $displayDefinition,
               onDismiss: {
                goToNextQuestion()

               }, content: {
                AnswerDefinitionScreen(model:
                                        AnswerDefinitionModel(question: definitionToDisplay!,
                                                              canGoToNextQuestion: true)
                )
               })
        .frame(maxHeight: .infinity)
        .onChange(of: selectedAnswerChoice, perform: { selected in
            onSelectAnswerChoice(selected: selected)
        })
        .onChange(of: currentQuestionIndex, perform: { _ in
            goToNextQuestion()
        })
        .onReceive(timer, perform: { _ in
            currentTick += 0.050
            currentTimeoutProgression = currentTick / appContext.maxTimeout
            if currentTimeoutProgression > 1 {
                uiState = .timeout
            }
        })
        .background {
            TimeoutBackgroundView(progress: currentTimeoutProgression)
        }
        .overlay {
            if uiState == .canceling {
                VStack {
                    Spacer()
                    GamePlayCancelGameView {
                        uiState = .inProgress
                    } onValid: {
                        cancelGame = true
                        uiState = .inProgress
                    }
                }
            } else if uiState == .success {
                VStack {
                    Spacer()
                    ResultBannerView(isValid: true,
                                     expectedTimeout: 2,
                                     isDisplayed: $displayResultBanner)
                        .edgesIgnoringSafeArea(.bottom)
                        .onTapGesture {
                            if uiState != .completed {
                                uiState = .completed
                                goToNextQuestion()
                            }
                        }
                }
            }
        }
        .toolbar {
            Button {
                uiState = .canceling
            } label: {
                R.image.close.image.foregroundColor(R.color.primary.color)
            }
        }
        .onChange(of: timeoutDisplayed, perform: { newValue in
            if newValue == false && uiState != .completed {
                goToNextQuestion()
            }
        })
        .onChange(of: displayResultBanner, perform: { newValue in
            if newValue == false && uiState != .completed {
                uiState = .completed
                goToNextQuestion()
            }
        })
        .onChange(of: uiState, perform: { newValue in
            switch newValue {
            case .timeout, .success, .failure:
                timer.upstream.connect().cancel()
            case .canceling:
                timer.upstream.connect().cancel()
            case .inProgress:
                timer = timer.upstream.autoconnect()
            default:
                break
            }
        })
        .onAppear {
            if !appContext.hasTimeout {
                timer.upstream.connect().cancel()
            }
        }
    }
}

#Preview {
    NavigationStack {
        GamePlayBoardView(questions: [
            QuestionChoice.companion.dummy,
            QuestionChoice.companion.dummy
        ], showResult: .constant(nil),
        cancelGame: .constant(false),
        showDefinition: .constant(nil),
        currentQuestionIndex: .constant(0),
        currentState: .success)
    }
    .environmentObject(AppContext(username: "DEBUG"))
    .environmentObject(Router())
}

#Preview {
    NavigationStack {
        GamePlayBoardView(questions: [
            QuestionChoice(id: "0",
                           content: "1\n2\n3\n4",
                           detail: "",
                           game: .all,
                           answers: QuestionChoice.companion.dummy.answers,
                           userSelectedAnswer: nil,
                           difficulty: .easy,
                           lang: .french,
                           illustration: nil,
                           responseTime: 0),
            QuestionChoice.companion.dummy
        ],
        showResult: .constant(nil),
        cancelGame: .constant(false),
        showDefinition: .constant(nil),
        currentQuestionIndex: .constant(0),
        currentState: .failure)
    }
    .preferredColorScheme(.dark)
    .environmentObject(AppContext(username: "DEBUG"))
    .environmentObject(Router())

}
