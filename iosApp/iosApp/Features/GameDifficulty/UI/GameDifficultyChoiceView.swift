//
//  GameDifficultyChoiceView.swift
//  CACDGAME
//
//  Created by François Dabonot on 17/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import Shared

struct GameDifficultyChoiceView: View {

    let difficulty: Difficulty
    @Binding var selectedDifficulty: Difficulty?

    init(difficulty: Difficulty,
         selectedDifficulty: Binding<Difficulty?>) {
        self.difficulty = difficulty
        self._selectedDifficulty = selectedDifficulty
    }

    var isSelected: Bool {
        selectedDifficulty?.id == difficulty.id
    }

    var backgroundContent: Color {
        if isSelected {
            return R.color.primary.color.opacity(0.12)
        } else {
            return R.color.unselectedContentColor.color
        }
    }

    var borderContent: Color {
        if isSelected {
            return R.color.primary.color
        } else {
            return R.color.unselectedBorderContentColor.color
        }
    }

    var body: some View {
        HStack {
            Image(isSelected ? difficulty.selectedimage() : difficulty.image())
                .frame(width: 57)
            VStack(spacing: 7) {
                Text(difficulty.title())
                    .font(R.font.poppinsExtraBold.font(size: 15))
                    .foregroundColor(isSelected ? R.color.primary.color : R.color.onSurface.color)
                    .frame(maxWidth: .infinity, alignment: .leading)
                HStack(alignment: .center) {
                    Text(difficulty.content())
                        .font(!isSelected ? R.font.robotoRegular.font(size: 15) : R.font.robotoBold.font(size: 15))
                        .foregroundColor(isSelected ? R.color.primary.color : R.color.onSurface.color)
                        .padding(.leading, 16)
                    Spacer()
                    R.image.check_good.image
                        .padding(.trailing, 16)
                        .foregroundColor(isSelected ? R.color.primary.color : R.color.unSelectedCheck.color)
                }
                .frame(
                    minWidth: 0,
                    maxWidth: .infinity,
                    minHeight: 0,
                    maxHeight: .infinity,
                    alignment: .leading
                )
                .background(backgroundContent)
                .overlay(
                    RoundedRectangle(cornerRadius: 8)
                        .stroke(borderContent,
                                lineWidth: 1)
                )
                .cornerRadius(8)
            }
        }
        .frame(maxWidth: .infinity, maxHeight: 70)
        .onTapGesture {
            selectedDifficulty = difficulty
        }
    }
}

#Preview {
    GameDifficultyChoiceView(difficulty: .easy,
                             selectedDifficulty: .constant(nil))
        .previewLayout(.sizeThatFits)
        .environmentObject(AppContext())
}

#Preview {
    GameDifficultyChoiceView(difficulty: .easy,
                             selectedDifficulty: .constant(.easy))
        .previewLayout(.sizeThatFits)
        .environmentObject(AppContext())
}
