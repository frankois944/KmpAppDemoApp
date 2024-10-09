//
//  GameDifficultySelectionView.swift
//  CACDGAME
//
//  Created by François Dabonot on 17/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import Shared

struct GameDifficultySelectionView: View {

    @Binding var selectedDifficulty: Difficulty?

    var body: some View {
        VStack(spacing: 30) {
            GameDifficultyChoiceView(difficulty: .easy,
                                     selectedDifficulty: $selectedDifficulty)
                .padding(.leading, 14)
                .padding(.trailing, 60)
            GameDifficultyChoiceView(difficulty: .medium,
                                     selectedDifficulty: $selectedDifficulty)
                .padding(.leading, 30)
                .padding(.trailing, 50)
            GameDifficultyChoiceView(difficulty: .hard,
                                     selectedDifficulty: $selectedDifficulty)
                .padding(.leading, 50)
                .padding(.trailing, 30)
        }
    }
}

#Preview {
    GameDifficultySelectionView(selectedDifficulty: .constant(.easy))
        .environmentObject(AppContext())
}
