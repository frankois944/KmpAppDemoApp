//
//  GameDifficultyScreen.swift
//  CACDGAME
//
//  Created by François Dabonot on 06/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import Shared

struct GameDifficultyScreen: View {

    static let route = GameDifficultyModel.self

    @EnvironmentObject var router: Router
    @EnvironmentObject var appContext: AppContext
    @State var selectedDifficulty: Difficulty?
    @State var canStartGame = false
    let model: GameDifficultyModel

    init(model: GameDifficultyModel,
         selectedDifficulty: Difficulty? = nil,
         canStartGame: Bool = false) {
        self.canStartGame = canStartGame
        self.selectedDifficulty = selectedDifficulty
        self.model = model
    }

    var body: some View {
        VStack {
            Spacer()
            GameDifficultyRuleView(username: appContext.username ?? "DEBUG")
                .padding()
            Spacer()
            GameDifficultySelectionView(selectedDifficulty: $selectedDifficulty)
            Spacer()
            NavigationFooterView(onGoForward: canStartGame ? {
                router.push(route: .gamePlay(model: .init(gameChoice: model.currentSelectedGame,
                                                          gameDifficulty: selectedDifficulty ?? .easy)))
            } : nil, onGoBack: {
                router.pop()
            })
            .opacity(canStartGame ? 1 : 0)
            Spacer()
        }
        .modifier(CommonCardBackground())
        .onChange(of: selectedDifficulty) { difficulty in
            Task { @MainActor in
                if let difficulty = difficulty {
                    let hasQuestion = try? await model.currentSelectedGame.hasQuestions(gameDifficulty: difficulty)
                    if hasQuestion?.boolValue == true {
                        canStartGame = true
                        return
                    }
                }
                canStartGame = false
            }
        }
    }
}

#Preview {
    GameDifficultyScreen(model: .init(currentSelectedGame: .design),
                         selectedDifficulty: .easy,
                         canStartGame: true)
        .environmentObject(AppContext())
        .environmentObject(Router())
}

#Preview {
    GameDifficultyScreen(model: .init(currentSelectedGame: .design),
                         selectedDifficulty: .easy,
                         canStartGame: true)
        .environmentObject(AppContext())
        .environmentObject(Router())
        .preferredColorScheme(.dark)
}
