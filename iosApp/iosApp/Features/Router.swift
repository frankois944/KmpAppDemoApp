//
//  AppRouter.swift
//  CACDGAME
//
//  Created by François Dabonot on 25/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import Foundation
import SwiftUI
import Shared

final class Router: ObservableObject {

    enum Route: Hashable {
        case home(model: HistoryModel),
             onBoarding(model: OnBoardingModel),
             gameSelection(model: GameSelectionModel),
             contact(model: ContactModel),
             contactFromResult(model: ContactModel),
             lexical(model: LexicalModel),
             lexicalDetail(model: AnswerDefinitionModel),
             setting(model: SettingsModel),
             gameDifficulty(model: GameDifficultyModel),
             gamePlay(model: GamePlayModel),
             answerDefinition(model: AnswerDefinitionModel),
             answerDefinitionFromGamePlay(model: AnswerDefinitionModel),
             gameResult(model: GameResultModel),
             gameResultFromHistory(model: GameResultModel),
             historyAnswers(model: HistoryModel)

        var value: any Hashable {
            switch self {
            case .home(let model):
                return model
            case .onBoarding(let model):
                return model
            case .gameSelection(let model):
                return model
            case .contact(let model):
                return model
            case .contactFromResult(let model):
                return model
            case .lexical(let model):
                return model
            case .lexicalDetail(let model):
                return model
            case .setting(let model):
                return model
            case .gameDifficulty(let model):
                return model
            case .gamePlay(let model):
                return model
            case .answerDefinition(let model):
                return model
            case .answerDefinitionFromGamePlay(let model):
                return model
            case .gameResult(let model):
                return model
            case .gameResultFromHistory(let model):
                return model
            case .historyAnswers(let model):
                return model
            }
        }
    }

    @Published var navPath: NavigationPath = .init()

    var stackSize: Int {
        navPath.count
    }

    func push(route: Router.Route) {
        navPath.append(route.value)
    }

    func pop(count: Int = 1) {
        if navPath.isEmpty == false {
            DispatchQueue.main.async { [weak self] in
                self?.navPath.removeLast(count)
            }
        } else {
            AppLogger.shared.e(messageString: "Try to pop view with enmpty stack")
        }
    }

    func popToRoot() {
        var antimation = Transaction()
        antimation.disablesAnimations = true
        withTransaction(antimation) {
            navPath = .init()
        }
    }
}
