//
//  SearchLexicalScreen.swift
//  CACDGAME
//
//  Created by François Dabonot on 06/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import Shared
import SwiftUIIntrospect

struct SearchLexicalScreen: View {

    static let route = SearchLexicalModel.self
    let onGoToNewGame: () -> Void

    @EnvironmentObject var router: Router
    @State var data: [QuestionChoice]?
    @State var previousSearchedCriteria: String = ""
    @State private var searchScope: SearchScope = .all
    @State var criteria: String = ""
    @State var design: Bool = false
    @State var product: Bool = false
    @State var tech: Bool = false
    @State private var searchJob: Task<(), Never>?
    @State private var hasNoHistory: Bool

    private let hasTestQuestion: Bool

    init(data: [QuestionChoice]? = nil, onGoToNewGame: @escaping () -> Void) {
        self.data = data
        self.hasTestQuestion = data != nil
        self.hasNoHistory = data?.isEmpty == true
        self.onGoToNewGame = onGoToNewGame
    }

    var body: some View {
        ZStack {
            if hasNoHistory {
                SearchLexicalEmptyView()
                    .frame(maxHeight: .infinity)
                    .overlay {
                        VStack {
                            PrimaryButton(R.string.localizable.start_new_quiz.localizedKey,
                                          action: onGoToNewGame)
                                .padding()
                        }
                        .frame(maxWidth: .infinity,
                               maxHeight: .infinity,
                               alignment: .bottom)
                    }
            } else {
                QuestionListingListView(questions: data ?? [], withBackground: true)
                    .searchable(text: $criteria)
                    .searchScopes(searchScope: $searchScope)
                    .onSubmit(of: .search, runSearch)
                    .onChange(of: criteria, perform: { _ in runSearch() })
                    .onChange(of: searchScope) { _ in runSearch() }
                    .font(R.font.robotoRegular.font(size: 17))
                    .onAppear {
                        runSearch()
                    }
            }
        }.task {
            guard !hasTestQuestion else { return }
            let nbHistory = try? await Database.companion.data.getHistoryCount()
            hasNoHistory = nbHistory ?? 0 == 0
        }
    }

    func runSearch() {
        guard !hasTestQuestion else { return }
        searchJob?.cancel()
        searchJob = nil
        searchJob = Task {
            do {
                if previousSearchedCriteria != criteria {
                    try await Task.sleep(for: .milliseconds(250))
                }
                guard let searchJob = searchJob, searchJob.isCancelled == false else { return }
                _ = await Task { @MainActor in
                    let productSelected = SearchScope.product.isSelected(searchScope.rawValue)
                    let designSelected = SearchScope.design.isSelected(searchScope.rawValue)
                    let techSelected = SearchScope.tech.isSelected(searchScope.rawValue)
                    let lang = AppContext.shared.currentLanguageCode
                    data = try await Database.companion.data.searchQuestions(criteria: criteria,
                                                                             productSelected: productSelected,
                                                                             designSelected: designSelected,
                                                                             techSelected: techSelected,
                                                                             lang: lang)
                    previousSearchedCriteria = criteria
                }.result
            } catch {
                AppLogger.shared.v(messageString: "Cant load LexicalScreen", throwable: error)
            }
        }
    }
}

#Preview {
    NavigationStack {
        SearchLexicalScreen(onGoToNewGame: {})
    }
    .environmentObject(AppContext())
    .environmentObject(Router())
}

#Preview {
    NavigationStack {
        SearchLexicalScreen(data: [QuestionChoice.companion.dummy], onGoToNewGame: {})
    }
    .environmentObject(AppContext())
    .environmentObject(Router())
}
