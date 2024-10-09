//
//  GameHistoryListView.swift
//  CACDGAME
//
//  Created by François Dabonot on 12/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import Shared

struct GameHistoryListView: View {

    let items: [HistoryData]
    @EnvironmentObject var router: Router

    var body: some View {
        ScrollView {
            LazyVStack(spacing: 10) {
                ForEach(Array(items.enumerated()), id: \.offset) { index, row in
                    GameHistoryItemView(item: row)
                        .onTapGesture {
                            trackEvent(name: "HistoryAnswers From Home")
                            router.push(route: Router.Route.gameResultFromHistory(model: .init(historyData: row,
                                                                                               fromGameResult: false)))
                        }
                        .if(index == (items.count - 1), true: {
                            $0.padding(.bottom, 72)
                        }, false: {
                            $0.padding([.top, .bottom], 5)
                        })
                        .padding([.trailing, .leading], 16)
                }
            }
        }
    }
}

#Preview {
    NavigationStack {
        GameHistoryListView(items: [HistoryData.companion.dummy, HistoryData.companion.dummy])
            .environmentObject(AppContext())
            .environmentObject(Router())
    }
}
