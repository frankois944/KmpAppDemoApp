//
//  MainScreen.swift
//  CACDGAME
//
//  Created by François Dabonot on 06/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import Combine
import SwiftUIIntrospect
import Shared
import CoreSpotlight

enum AppTab: Int {
    case profile = 0, lexical = 1, game = 2, contact = 3, settings = 4

    var title: LocalizedStringKey {
        switch self {
        case .profile:
            return R.string.localizable.history_title.localizedKey
        case .lexical:
            return R.string.localizable.lexical_header.localizedKey
        case .game:
            return R.string.localizable.quiz_selection_title.localizedKey
        case .contact:
            return R.string.localizable.title_contacter_cacd2.localizedKey
        case .settings:
            return R.string.localizable.title_parameter.localizedKey
        }
    }

}

struct AppRoot: View {

    @Environment(\.colorScheme) var systemColorScheme
    @EnvironmentObject var theme: ThemeManager
    @EnvironmentObject var appContext: AppContext
    @State var displayConsent: Bool = false
    @State var fullFetchDataNeeded: Bool = false
    @State var initialDataLoaded: Bool = false
    @State var updateMode: UpdateMode?
    @State var isFetching: Bool = false

    private func doSlientUpdate() {
        if updateMode == .silent && isFetching == false {
            isFetching = true
            Task {
                do {
                    try await DatoCMSAPI.shared.fetchAllData(language: appContext.currentLanguageCode)
                    try await indexingDataForSpotLight(language: appContext.currentLanguageCode)
                } catch {
                    AppLogger.shared.e(messageString: "SILENT UPDATE FAILED", throwable: error)
                }
                isFetching = false
            }
        }
    }

    @discardableResult
    private func checkUpdate() -> Bool {
        guard updateMode == nil, !displayConsent else { return false }
        Task {
            let cLang = appContext.currentLanguageCode
            updateMode = try? await DatoCMSAPI.shared.updateModeFor(language: cLang)
        }
        return updateMode != nil
    }

    var body: some View {
        MainContainer()
            .onAppear {
                displayConsent = !appContext.consentManagerDisplayed
                if !displayConsent {
                    checkUpdate()
                }
            }
            .sheet(isPresented: $displayConsent) {
                ConsentManagerScreen()
                    .mpresentationBackground(.clear)
                    .interactiveDismissDisabled()
                    .onDisappear {
                        appContext.consentManagerDisplayed = true
                        checkUpdate()
                    }
                    .analyticsScreen(name: "ConsentManagerFromAppStart",
                                     class: "ConsentManagerFromAppStart")
            }
            .fullScreenCover(isPresented: $fullFetchDataNeeded) {
                LoadQuestionsDataView(language: appContext.currentLanguageCode) {
                    fullFetchDataNeeded = false
                }
            }
            .onChange(of: appContext.currentLanguageCode, perform: { _ in
                if checkUpdate() == false {
                    Task {
                        do {
                            try await indexingDataForSpotLight(language: appContext.currentLanguageCode)
                        } catch {
                            AppLogger.shared.e(messageString: "INDEXING CONTENT FAILED",
                                               throwable: error)
                        }
                    }
                }
            })
            .onChange(of: updateMode) { newValue in
                if let newValue = newValue {
                    switch newValue {
                    case .full:
                        fullFetchDataNeeded = true
                    case .silent:
                        doSlientUpdate()
                    default:
                        break
                    }
                    updateMode = nil
                }
            }
    }
}

struct MainContainer: View {

    @EnvironmentObject var appContext: AppContext
    @EnvironmentObject var theme: ThemeManager
    @State private var currentTab = AppTab.game
    @StateObject private var router = Router()

    var body: some View {
        NavigationStack(path: $router.navPath) {
            if let username = appContext.username, !username.isEmpty {
                TabView(selection: $currentTab) {
                    NavigationView {
                        GameSelectionScreen()
                            .navigationBarTitle(AppTab.game.title)
                            .navigationBarTitleDisplayMode(.inline)
                            .analyticsScreen(name: "GameSelection",
                                             class: "GameSelection")
                    }
                    .tabItem {
                        Label(R.string.localizable.tab_quiz.localizedKey,
                              image: R.image.game.name)
                    }
                    .tag(AppTab.game)
                    NavigationView {
                        LexicalScreen(onGoToNewGame: {
                            trackEvent(name: "GameSelection from History")
                            currentTab = .game
                        })
                        .navigationBarTitle(AppTab.lexical.title)
                        .navigationBarTitleDisplayMode(.inline)
                        .analyticsScreen(name: "LexicalQuestionListing",
                                         class: "LexicalQuestionListing")
                    }
                    .tabItem {
                        Label(R.string.localizable.tab_lexique.localizedKey,
                              image: R.image.lexical.name)
                    }
                    .tag(AppTab.lexical)
                    NavigationView {
                        ContactScreen()
                            .navigationBarTitle(AppTab.contact.title)
                            .navigationBarTitleDisplayMode(.inline)
                            .analyticsScreen(name: "ContactFromTab",
                                             class: "ContactFromTab")
                    }
                    .tabItem {
                        Label(R.string.localizable.tab_contact.localizedKey,
                              image: R.image.contact.name)
                    }
                    .tag(AppTab.contact)
                    NavigationView {
                        HistoryScreen(onGoToNewGame: {
                            trackEvent(name: "GameSelection from History")
                            currentTab = .game
                        })
                        .navigationBarTitle(AppTab.profile.title)
                        .navigationBarTitleDisplayMode(.inline)
                        .analyticsScreen(name: "History",
                                         class: "History")
                    }
                    .tabItem {
                        Label(R.string.localizable.history_title.localizedKey,
                              image: R.image.history.name)
                    }
                    .tag(AppTab.profile)
                    NavigationView {
                        SettingsScreen()
                            .navigationBarTitle(AppTab.settings.title)
                            .navigationBarTitleDisplayMode(.inline)
                            .analyticsScreen(name: "Setting",
                                             class: "Setting")
                    }
                    .tabItem {
                        Label(R.string.localizable.tab_parametre.localizedKey,
                              image: R.image.settings.name)
                    }
                    .tag(AppTab.settings)
                }
                .navigationBarTitleDisplayMode(.inline)
                .navigationDestination(for: GameDifficultyScreen.route) {
                    GameDifficultyScreen(model: $0)
                        .navigationTitle(R.string.localizable.title_difficulty.localizedKey)
                        .navigationBarItems(leading: BtnBack())
                        .hasBackGestion()
                        .analyticsScreen(name: "GameDifficulty",
                                         class: "GameDifficulty",
                                         extraParameters: [
                                            "game": $0.currentSelectedGame.label().stringKey ?? "N/A"
                                         ])
                }
                .navigationDestination(for: AnswerDefinitionScreen.route) { data in
                    AnswerDefinitionScreen(model: data)
                        .navigationTitle(R.string.localizable.title_definition.localizedKey)
                        .navigationBarItems(leading: BtnBack())
                        .if(data.canGoToNextQuestion, true: {
                            $0.analyticsScreen(name: "AnswerDefinitionFromGamePlay",
                                               class: "AnswerDefinitionFromGamePlay",
                                               extraParameters: ["questionId": "\(data.question.id)"])
                        }, false: {
                            $0.analyticsScreen(name: "AnswerDefinition",
                                               class: "AnswerDefinition",
                                               extraParameters: ["questionId": "\(data.question.id)"])
                        })
                        .if(data.canGoToNextQuestion, false: {
                            $0.hasBackGestion()
                        })
                }
                .navigationDestination(for: ContactScreen.route) { _ in
                    ContactScreen()
                        .navigationTitle(
                            R.string.localizable.title_contacter_cacd2.localizedKey
                        )
                        .navigationBarItems(leading: BtnBack())
                        .hasBackGestion()
                        .analyticsScreen(name: "ContactFromResult",
                                         class: "ContactFromResult")
                }
                .navigationDestination(for: GameResultScreen.route) {
                    GameResultScreen(model: $0)
                        .navigationBarBackButtonHidden(true)
                        .navigationTitle(
                            R.string.localizable.result_title.localizedKey
                        )
                        .if($0.fromGameResult, false: {
                            $0.navigationBarItems(leading: BtnBack())
                        })
                        .if($0.fromGameResult, true: {
                            $0.analyticsScreen(name: "GameResult",
                                               class: "GameResult")
                        }, false: {
                            $0.analyticsScreen(name: "GameResultFromHistory",
                                               class: "GameResultFromHistory")
                        })
                        .if($0.fromGameResult, false: {
                            $0.hasBackGestion()
                        })
                }
                .navigationDestination(for: GamePlayScreen.route) {
                    GamePlayScreen(model: $0)
                        .navigationBarBackButtonHidden(true)
                        .navigationTitle($0.gameChoice.title())
                        .analyticsScreen(name: "GamePlay",
                                         class: "GamePlay",
                                         extraParameters: [
                                            "game": $0.gameChoice.label().stringKey ?? "N/A",
                                            "gamedifficulty": $0.gameDifficulty.label()
                                         ])
                }
                .navigationDestination(for: QuestionListingScreen.route) { data in
                    let extraParams = { () -> [String: String] in
                        if let isSuccess = data.isSuccess {
                            return ["type": isSuccess ? "ONLY_CORRECT" : "ONLY_ERROR"]
                        } else {
                            return ["type": "ALL"]
                        }
                    }
                    QuestionListingScreen(model: data)
                        .navigationTitle(data.title)
                        .navigationBarItems(leading: BtnBack())
                        .hasBackGestion()
                        .analyticsScreen(name: "HistoryQuestionListing",
                                         class: "HistoryQuestionListing",
                                         extraParameters: extraParams())
                }
                .navigationBarTitleDisplayMode(.inline)
                .navigationDestination(for: GameDifficultyScreen.route) {
                    GameDifficultyScreen(model: $0)
                        .navigationTitle(R.string.localizable.title_difficulty.localizedKey)
                        .navigationBarItems(leading: BtnBack())
                        .hasBackGestion()
                        .analyticsScreen(name: "GameDifficulty",
                                         class: "GameDifficulty",
                                         extraParameters: [
                                            "game": $0.currentSelectedGame.label().stringKey ?? "N/A"
                                         ])
                }
                .navigationDestination(for: AnswerDefinitionScreen.route) { data in
                    AnswerDefinitionScreen(model: data)
                        .navigationTitle(R.string.localizable.title_definition.localizedKey)
                        .navigationBarItems(leading: BtnBack())
                        .if(data.canGoToNextQuestion, true: {
                            $0.analyticsScreen(name: "AnswerDefinitionFromGamePlay",
                                               class: "AnswerDefinitionFromGamePlay",
                                               extraParameters: ["questionId": "\(data.question.id)"])
                        }, false: {
                            $0.analyticsScreen(name: "AnswerDefinition",
                                               class: "AnswerDefinition",
                                               extraParameters: ["questionId": "\(data.question.id)"])
                        })
                        .if(data.canGoToNextQuestion, false: {
                            $0.hasBackGestion()
                        })
                }
                .navigationDestination(for: ContactScreen.route) { _ in
                    ContactScreen()
                        .navigationTitle(
                            R.string.localizable.title_contacter_cacd2.localizedKey
                        )
                        .navigationBarItems(leading: BtnBack())
                        .hasBackGestion()
                        .analyticsScreen(name: "ContactFromResult",
                                         class: "ContactFromResult")
                }
                .navigationDestination(for: GameResultScreen.route) {
                    GameResultScreen(model: $0)
                        .navigationBarBackButtonHidden(true)
                        .navigationTitle(
                            R.string.localizable.result_title.localizedKey
                        )
                        .if($0.fromGameResult, false: {
                            $0.navigationBarItems(leading: BtnBack())
                        })
                        .if($0.fromGameResult, true: {
                            $0.analyticsScreen(name: "GameResult",
                                               class: "GameResult")
                        }, false: {
                            $0.analyticsScreen(name: "GameResultFromHistory",
                                               class: "GameResultFromHistory")
                        })
                        .if($0.fromGameResult, false: {
                            $0.hasBackGestion()
                        })
                }
                .navigationDestination(for: GamePlayScreen.route) {
                    GamePlayScreen(model: $0)
                        .navigationBarBackButtonHidden(true)
                        .navigationTitle($0.gameChoice.title())
                        .analyticsScreen(name: "GamePlay",
                                         class: "GamePlay",
                                         extraParameters: [
                                            "game": $0.gameChoice.label().stringKey ?? "N/A",
                                            "gamedifficulty": $0.gameDifficulty.label()
                                         ])
                }
                .navigationDestination(for: QuestionListingScreen.route) { data in
                    let extraParams = { () -> [String: String] in
                        if let isSuccess = data.isSuccess {
                            return ["type": isSuccess ? "ONLY_CORRECT" : "ONLY_ERROR"]
                        } else {
                            return ["type": "ALL"]
                        }
                    }
                    QuestionListingScreen(model: data)
                        .navigationTitle(data.title)
                        .navigationBarItems(leading: BtnBack())
                        .hasBackGestion()
                        .analyticsScreen(name: "HistoryQuestionListing",
                                         class: "HistoryQuestionListing",
                                         extraParameters: extraParams())
                }
            } else {
                OnBoardingScreen {
                    appContext.username = $0
                }
                .analyticsScreen(name: "OnBoarding",
                                 class: "OnBoarding")
            }
        }
        // .navigationBarTitleDisplayMode(.inline)
        .environmentObject(router)
        .onContinueUserActivity(CSSearchableItemActionType, perform: handleSpotlight)
    }

    func handleSpotlight(userActivity: NSUserActivity) {
        guard let uniqueIdentifier = userActivity.userInfo?[CSSearchableItemActivityIdentifier] as? String else {
            return
        }

        AppLogger.shared.d(messageString: "Item tapped: \(uniqueIdentifier)")
        trackEvent(name: "Open App from spotlight", extra: ["questionID": uniqueIdentifier])
        Task { @MainActor in
            do {
                let question = try await Database.companion.data.getQuestion(questionId: uniqueIdentifier,
                                                                             lang: appContext.currentLanguageCode)
                router.push(route: .answerDefinition(model: AnswerDefinitionModel(question: question,
                                                                                  canGoToNextQuestion: false)))
            } catch {
                AppLogger.shared.e(messageString: "Cant open question from search", throwable: error)
            }
        }

    }
}

#Preview {
    AppRoot()
        .environmentObject(AppContext())
}
