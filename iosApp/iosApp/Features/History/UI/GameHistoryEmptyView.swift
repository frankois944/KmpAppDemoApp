//
//  GameHistoryEmptyView.swift
//  CACDGAME
//
//  Created by François Dabonot on 06/12/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI

struct GameHistoryEmptyView: View {
    var body: some View {
        VStack(alignment: .center, content: {
            R.image.history_empty.image
            Text(R.string.localizable.history_emptylist.localizedKey)
                .multilineTextAlignment(.center)
                .padding()
                .font(R.font.poppinsSemiBold.font(size: 18))

        })
    }
}

#Preview {
    GameHistoryEmptyView()
}
