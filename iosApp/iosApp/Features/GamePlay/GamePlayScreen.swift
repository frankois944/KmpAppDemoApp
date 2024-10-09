//
//  GamePlayScreen.swift
//  CACDGAME
//
//  Created by François Dabonot on 06/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import Shared

struct GamePlayScreen: View {

    static let route = GamePlayModel.self

    @EnvironmentObject var appContext: AppContext
    @EnvironmentObject var router: Router
    @State var questions: [QuestionChoice]?
    @State var displayGameLoader = true
    @State var resultData: [QuestionChoice]?
    @State var cancelGame: Bool = false
    @State var displayDefinition: QuestionChoice?
    @State var currentQuestionIndex: Int = 0
    @State var historyId: Int = 0
    let model: GamePlayModel

    init(model: GamePlayModel,
         questions: [QuestionChoice]? = nil) {
        self.questions = questions
        self.model = model
    }

    var body: some View {
        VStack {
            if displayGameLoader {
                GamePlayLaunchView(initialSeconds: 3, isDisplayed: $displayGameLoader)
            } else if let questions = questions {
                GamePlayBoardView(questions: questions,
                                  showResult: $resultData,
                                  cancelGame: $cancelGame,
                                  showDefinition: $displayDefinition,
                                  currentQuestionIndex: $currentQuestionIndex)
            }
        }
        .task {
            do {
                #if DEBUG
                if questions == nil {
                    questions = try await model.gameChoice.getQuestions(gameDifficulty: model.gameDifficulty,
                                                                        maxQuestion: 3)
                }
                #else
                let size = KotlinLong(value: GameChoice.companion.maxQuestion)
                questions = try await model.gameChoice.getQuestions(gameDifficulty: model.gameDifficulty,
                                                                    maxQuestion: size)
                #endif
            } catch {
                AppLogger.companion.e(messageString: "Cant load game", throwable: error)
                router.pop()
            }
        }
        .onChange(of: cancelGame) { newValue in
            if newValue == true {
                router.pop()
            }
        }
        .onChange(of: resultData) { _ in
            Task { @MainActor in
                do {
                    let result = try await Database.companion.data.saveHistory(category: model.gameChoice,
                                                                               questions: questions!)
                    historyId = Int(truncating: result)
                    router.push(route: .gameResult(model: .init(historyId: historyId,
                                                                fromGameResult: true,
                                                                showResultLoader: true,
                                                                shouldDisplayResultBanner: true)))
                } catch {
                    AppLogger.companion.e(messageString: "Cant save history", throwable: error)
                    router.pop()
                }
            }
        }
        .if(displayGameLoader, true: { view in
            view.toolbar(.hidden, for: .navigationBar)
        }, false: { view in
            view.toolbar(.visible, for: .navigationBar)
        })
    }
}

#Preview {
    GamePlayScreen(model: .init(gameChoice: .design,
                                gameDifficulty: .easy),
                   questions: [QuestionChoice.companion.dummy])
        .environmentObject(AppContext(username: "DEBUG"))
        .environmentObject(Router())
}
