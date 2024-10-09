//
//  ContactSentView.swift
//  CACDGAME
//
//  Created by François Dabonot on 28/09/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI

struct ContactSentView: View {

    @State private var timer = Timer.publish(every: 1, on: .main, in: .common).autoconnect()
    @State var startTime = Date.now
    @Binding var isDisplayed: Bool?
    let expectedTimeout: TimeInterval

    init(expectedTimeout: TimeInterval = 4,
         isDisplayed: Binding<Bool?>) {
        self.expectedTimeout = expectedTimeout
        self._isDisplayed = isDisplayed
    }
    var body: some View {
        VStack {
            Spacer()
            VStack(alignment: .center) {
                Text(R.string.localizable.contact_result_message.localizedKey)
                    .font(R.font.poppinsSemiBold.font(size: 16))
                    .lineSpacing(8)
                    .multilineTextAlignment(.center)
                    .padding(.top, 100)
                    .padding(.bottom, 40)
                    .padding([.leading, .trailing])
            }
            .overlay {
                R.image.thanks.image
                    .padding(.bottom, 330)
            }
            .background(
                R.color.surface.color
                    .cornerRadius(16)
                    .shadow(color: R.color.onSurface.color.opacity(0.20),
                            radius: 4)
            )
        }
        .background(R.color.surface.color.opacity(0.7))
        .onReceive(timer) { _ in
            let cTime = -startTime.timeIntervalSinceNow
            isDisplayed = cTime <= expectedTimeout
        }
    }
}

#Preview {
    VStack {
        ContactSentView(isDisplayed: .constant(true))
    }.background(.red)
}
