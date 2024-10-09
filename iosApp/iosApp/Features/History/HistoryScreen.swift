//
//  HomeScreen.swift
//  CACDGAME
//
//  Created by François Dabonot on 06/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import SwiftUIIntrospect
import Shared

struct HistoryScreen: View {

    static let route = HistoryModel.self

    let onGoToNewGame: () -> Void
    @EnvironmentObject var router: Router

    var body: some View {
        VStack {
            GameHistoryView().frame(maxHeight: .infinity)
        }.overlay {
            VStack {
                PrimaryButton(R.string.localizable.start_new_quiz.localizedKey,
                              action: onGoToNewGame)
                    .padding()
            }.frame(maxWidth: .infinity,
                    maxHeight: .infinity,
                    alignment: .bottom)
        }
        .modifier(CommonCardBackground())
    }
}

#Preview {
    HistoryScreen(onGoToNewGame: {})
        .environmentObject(AppContext())
        .environmentObject(Router())
}
