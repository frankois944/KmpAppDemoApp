//
//  GameDifficultyRuleView.swift
//  CACDGAME
//
//  Created by François Dabonot on 17/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI

struct GameDifficultyRuleView: View {

    let username: String

    var body: some View {
        CustomAttributedText(stringRes: R.string.localizable.difficulty_description.key,
                             custom: [
                                .DEFAULT: .init(font: R.font.poppinsMedium.font(size: 15)),
                                .ITALIC: .init(font: R.font.poppinsBold.font(size: 15))
                             ],
                             username)
            .layoutPriority(1)
            .fixedSize(horizontal: false, vertical: true)
    }
}

#Preview {
    GameDifficultyRuleView(username: "DEBUG")
        .environmentObject(AppContext())
}
