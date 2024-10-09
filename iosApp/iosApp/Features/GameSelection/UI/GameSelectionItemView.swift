//
//  GameSelectionItemView.swift
//  CACDGAME
//
//  Created by François Dabonot on 12/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import Shared

struct GameSelectionItemView: View {

    @Binding var selectedGame: GameChoice?
    let choice: GameChoice
    let isSelected: Bool

    init(choice: GameChoice, selectedGame: Binding<GameChoice?>) {
        self.choice = choice
        self._selectedGame = selectedGame
        self.isSelected = choice.gameId == selectedGame.wrappedValue?.gameId
    }

    var background: Color {
        if isSelected {
            return R.color.selectedPrimary.color
        } else {
            return R.color.unselectedContentColor.color
        }
    }

    var border: Color {
        if isSelected {
            return R.color.primary.color
        } else {
            return R.color.unselectedBorderContentColor.color
        }
    }

    var textStyle: Font {
        if isSelected {
            return R.font.robotoBold.font(size: 15)
        } else {
            return R.font.robotoRegular.font(size: 15)
        }
    }

    var body: some View {
        ZStack {
            RoundedCorners(background: background,
                           border: border,
                           topLeft: 12,
                           topRight: 4,
                           bottomLeft: 4,
                           bottomRight: 12)
            VStack(alignment: .center) {
                R.image.check_good.image
                    .foregroundColor(R.color.primary.color)
                    .opacity(isSelected ? 1 : 0)
                    .padding([.top, .trailing], 10)
                    .frame(maxWidth: .infinity, alignment: .trailing)
                Spacer()
                Image(choice.image(selectedGameId: selectedGame))
                Spacer()
                Text(choice.title())
                    .foregroundColor(choice.color(selectedGameId: selectedGame))
                    .font(textStyle)
                Spacer()
            }
        }
        .onTapGesture {
            selectedGame = choice
        }
    }
}

#Preview {
    GameSelectionItemView(choice: .design,
                          selectedGame: .constant(.design))
        .aspectRatio(1, contentMode: .fit)
        .previewLayout(PreviewLayout.sizeThatFits)
        .environmentObject(AppContext())
}

#Preview {
    GameSelectionItemView(choice: .design,
                          selectedGame: .constant(nil))
        .aspectRatio(1, contentMode: .fit)
        .previewLayout(PreviewLayout.sizeThatFits)
        .environmentObject(AppContext())
}
