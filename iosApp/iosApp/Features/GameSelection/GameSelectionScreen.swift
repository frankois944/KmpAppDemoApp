//
//  GameSelectionScreen.swift
//  CACDGAME
//
//  Created by François Dabonot on 06/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import Shared

struct GameSelectionScreen: View {

    static let route = GameSelectionModel.self

    @EnvironmentObject var router: Router
    @EnvironmentObject var appContext: AppContext
    @State var currentSelectedGame: GameChoice?

    var ruleId: Int {
        (appContext.username?.hashValue ?? 0) +
            appContext.currentLanguageCode.hashValue +
            appContext.screenMode.hashValue
    }

    var body: some View {
        VStack {
            Spacer()
            GameSelectionRuleView(username: appContext.username ?? "DEBUG")
                .id(ruleId)
                .padding()
            Spacer()
            GameSelectionSelectorView(currentSelectedGame: $currentSelectedGame)
                .padding([.trailing, .leading])
            Spacer()
            NavigationFooterView(onGoForward: {
                router.push(route: .gameDifficulty(model: .init(currentSelectedGame: currentSelectedGame!)))
            })
            .opacity(currentSelectedGame == nil ? 0 : 1)
            Spacer()
        }
        .modifier(CommonCardBackground())
    }
}

#Preview {
    NavigationStack {
        GameSelectionScreen()
    }
    .environmentObject(AppContext(username: "toto"))
    .environmentObject(Router())
}

#Preview {
    NavigationStack {
        GameSelectionScreen()
    }
    .environmentObject(AppContext(username: "toto"))
    .environmentObject(Router())
}
