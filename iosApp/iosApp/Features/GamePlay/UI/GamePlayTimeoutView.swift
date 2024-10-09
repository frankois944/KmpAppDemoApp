//
//  GamePlayTimeoutView.swift
//  CACDGAME
//
//  Created by François Dabonot on 19/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI

struct GamePlayTimeoutView: View {

    @EnvironmentObject private var appContext: AppContext
    @State var startTime = Date.now
    @Binding var isDisplayed: Bool?

    private let expectedTimeout: TimeInterval = 2
    private let timer = Timer.publish(every: 0.1, on: .main, in: .common).autoconnect()

    init(isDisplayed: Binding<Bool?>) {
        self._isDisplayed = isDisplayed
    }

    var body: some View {
        VStack {
            Text(R.string.localizable.timeout_message(appContext.username!))
                .font(R.font.poppinsExtraBold.font(size: 37))
                .multilineTextAlignment(.center)
                .foregroundColor(R.color.onSurface.color)
            R.image.timeout.image
                .foregroundColor(R.color.primary.color)
        }
        .onReceive(timer, perform: { _ in
            let cTime = -startTime.timeIntervalSinceNow
            isDisplayed = cTime <= expectedTimeout
        })
    }
}

#Preview {
    GamePlayTimeoutView(isDisplayed: .constant(true))
        .environmentObject(AppContext(username: "DEBUG"))
}
