//
//  GamePlayProgressView.swift
//  CACDGAME
//
//  Created by François Dabonot on 19/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI

struct GamePlayProgressView: View {

    @EnvironmentObject var appContext: AppContext

    let currentQuestion: Int
    let totalQuestion: Int

    var body: some View {
        ProgressView(value: Double(currentQuestion + 1), total: Double(totalQuestion))
            .accentColor(R.color.primary.color)
            .tint(R.color.primary.color)
    }
}

#Preview {
    VStack {
        GamePlayProgressView(currentQuestion: 5, totalQuestion: 10)
        Spacer()
    }
    .previewLayout(.sizeThatFits)
    .environmentObject(AppContext(username: "DEBUG"))
}
