//
//  GameResultLoaderView.swift
//  CACDGAME
//
//  Created by François Dabonot on 24/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI

struct GameResultLoaderView: View {

    @EnvironmentObject var appContext: AppContext
    @State var initialSeconds: TimeInterval
    @State private var currentTimeContent: Int = 0
    @State var startTime = Date.now
    @Binding var isDisplayed: Bool

    private let timer = Timer.publish(every: 1, on: .main, in: .common).autoconnect()

    var body: some View {
        VStack {
            Spacer()
            VStack {
                CustomAttributedText(stringRes: R.string.localizable.result_loader_message.key,
                                     custom: [
                                        .ITALIC: .init(font: R.font.poppinsExtraBold.font(size: 37)),
                                        .DEFAULT: .init(font: R.font.poppinsExtraBold.font(size: 37))
                                     ])
                    .lineLimit(2, reservesSpace: true)
                    .multilineTextAlignment(.center)
                Text("\(currentTimeContent)")
                    .font(R.font.poppinsSemiBold.font(size: 96))
                    .foregroundColor(R.color.primary.color)
            }.padding()
            Spacer()
            R.image.greatIdea.image
            Spacer()
            R.image.small_logo.image
            Spacer()
        }
        .background(Color(uiColor: UIColor.systemBackground))
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .onReceive(timer, perform: { _ in
            let cTime = -startTime.timeIntervalSinceNow
            currentTimeContent = Int((initialSeconds - cTime).rounded(.awayFromZero))
            isDisplayed = currentTimeContent > 0
        })
        .onAppear {
            currentTimeContent = Int(initialSeconds)
        }
    }
}

#Preview {
    GameResultLoaderView(initialSeconds: 5, isDisplayed: .constant(false))
        .environmentObject(AppContext(username: "DEBUG"))
}

#Preview {
    GameResultLoaderView(initialSeconds: 5, isDisplayed: .constant(false))
        .environmentObject(AppContext(username: "DEBUG"))
}
