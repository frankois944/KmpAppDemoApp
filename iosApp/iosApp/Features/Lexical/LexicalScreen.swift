//
//  LexicalScreen.swift
//  CACDGAME
//
//  Created by François Dabonot on 06/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import Shared
import SwiftUIIntrospect

struct LexicalScreen: View {

    static let route = LexicalModel.self
    let onGoToNewGame: () -> Void

    var body: some View {
        SearchLexicalScreen(onGoToNewGame: onGoToNewGame)
            .modifier(CommonCardBackground(padding: 0))
    }
}

#Preview {
    NavigationStack {
        LexicalScreen(onGoToNewGame: {})
    }
    .environmentObject(AppContext())
    .environmentObject(Router())
}
