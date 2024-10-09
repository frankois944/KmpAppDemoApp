//
//  ResultBannerView.swift
//  CACDGAME
//
//  Created by frankois on 13/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI

struct ResultBannerView: View {

    @State private var timer = Timer.publish(every: 1, on: .main, in: .common).autoconnect()
    @State var startTime = Date.now
    @Binding var isDisplayed: Bool?
    let message: String?
    let expectedTimeout: TimeInterval
    let isValid: Bool

    init(isValid: Bool,
         message: String? = nil,
         expectedTimeout: TimeInterval = 2,
         isDisplayed: Binding<Bool?>) {
        self.expectedTimeout = expectedTimeout
        self.message = message
        self.isValid = isValid
        self._isDisplayed = isDisplayed
    }

    var body: some View {
        VStack {
            Spacer()
            if isValid {
                ResultOKBannerView(message: message)
                    .background(R.color.surface.color)
            } else {
                ResultKOBannerView(message: message)
                    .background(R.color.surface.color)
            }
        }
        //
        .onReceive(timer) { _ in
            let cTime = -startTime.timeIntervalSinceNow
            isDisplayed = cTime <= expectedTimeout
        }
    }
}

#Preview {
    ResultBannerView(isValid: true, isDisplayed: .constant(true))
        .environmentObject(AppContext(username: "DEBUG"))
}

#Preview {
    ResultBannerView(isValid: false, isDisplayed: .constant(true))
        .environmentObject(AppContext(username: "DEBUG"))
}
