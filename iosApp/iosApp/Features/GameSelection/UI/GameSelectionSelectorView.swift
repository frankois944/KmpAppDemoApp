//
//  GameSelectionSelectorView.swift
//  CACDGAME
//
//  Created by François Dabonot on 12/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import Shared

struct GameSelectionSelectorView: View {

    @Binding var currentSelectedGame: GameChoice?

    init(currentSelectedGame: Binding<GameChoice?>) {
        self._currentSelectedGame = currentSelectedGame
    }

    var body: some View {
        VStack(spacing: 10) {
            HStack(spacing: 10) {
                GameSelectionItemView(choice: .design,
                                      selectedGame: $currentSelectedGame)
                    .aspectRatio(1, contentMode: .fit)
                GameSelectionItemView(choice: .tech,
                                      selectedGame: $currentSelectedGame)
                    .aspectRatio(1, contentMode: .fit)
            }
            HStack(spacing: 10) {
                GameSelectionItemView(choice: .product,
                                      selectedGame: $currentSelectedGame)
                    .aspectRatio(1, contentMode: .fit)
                GameSelectionItemView(choice: .all,
                                      selectedGame: $currentSelectedGame)
                    .aspectRatio(1, contentMode: .fit)
            }
        }
    }
}

#Preview {
    GameSelectionSelectorView(currentSelectedGame: .constant(.tech))
        .preferredColorScheme(.light)
        .environmentObject(AppContext())
}

#Preview {

    GameSelectionSelectorView(currentSelectedGame: .constant(.design))
        .preferredColorScheme(.dark)
        .environmentObject(AppContext())
}
