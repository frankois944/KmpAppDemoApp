//
//  SwiftUIView.swift
//  CACDGAME
//
//  Created by François Dabonot on 12/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import Shared

struct GameHistoryView: View {

    @EnvironmentObject var appContext: AppContext
    @EnvironmentObject var router: Router
    @State private var questions: [HistoryData]?
    @State private var filterExpanded: Bool = false
    @State private var criteria: HistoryFilterCriteria = .init(designSelected: true,
                                                               techSelected: true,
                                                               productSelected: true,
                                                               mixSelected: true)
    @State private var latestCriteria: HistoryFilterCriteria?
    @State private var listener: CommonFlow<NSArray>?
    @State private var hasNoHistory: Bool = false

    private let hasTestQuestion: Bool

    init(questions: [HistoryData]? = nil) {
        self.questions = questions
        self.hasTestQuestion = questions != nil
        self.hasNoHistory = questions?.isEmpty == true
    }

    private func watchList() {
        if !hasTestQuestion && (criteria != latestCriteria || listener == nil) {
            latestCriteria = criteria
            AppLogger.companion.v(messageString: "\(criteria)")
            AppLogger.companion.v(messageString: "\(criteria.buildCategoryIdList())")
            listener = Database.companion.data.getAllHistoryWithFlow(criteria: criteria)
            listener?.watch { content in
                questions = (content as? [HistoryData]) ?? []
            }
        }
    }

    var body: some View {
        VStack {
            if hasNoHistory {
                GameHistoryEmptyView()
            } else {
                GameHistoryListView(items: questions ?? [])
                    .toolbar {
                        ToolbarItem(placement: .navigationBarTrailing) {
                            Button {
                                filterExpanded = !filterExpanded
                            } label: {
                                R.image.filter.image
                                    .foregroundColor(R.color.primary.color)
                            }.popover(isPresented: $filterExpanded) {
                                HistoryFilterMenuView(filterCriteria: $criteria)
                                    .popoverPresentationCompactAdaptation()
                            }
                        }
                    }
                    .onChange(of: criteria) { _ in
                        watchList()
                    }
                    .onAppear {
                        watchList()
                    }
            }
        }
        .task {
            guard !hasTestQuestion else { return }
            let nbHistory = try? await Database.companion.data.getHistoryCount()
            hasNoHistory = nbHistory ?? 0 == 0
        }
    }
}

#Preview {
    GameHistoryView(questions: [
        HistoryData.companion.dummy,
        HistoryData.companion.dummy,
        HistoryData.companion.dummy
    ])
    .environmentObject(AppContext())
    .environmentObject(Router())
}

#Preview {
    GameHistoryView(questions: nil)
        .environmentObject(AppContext())
        .environmentObject(Router())
}
