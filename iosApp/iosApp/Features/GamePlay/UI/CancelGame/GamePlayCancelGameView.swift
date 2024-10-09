//
//  GamePlayCancelGameView.swift
//  CACDGAME
//
//  Created by François Dabonot on 19/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI

struct GamePlayCancelGameView: View {

    @EnvironmentObject var appContext: AppContext
    let onCancel: () -> Void
    let onValid: () -> Void

    var body: some View {
        VStack {
            Spacer()
            VStack(alignment: .center) {
                Text(R.string.localizable.quit_game_message.localizedKeyWithFormat(appContext.username!))
                    .font(R.font.poppinsSemiBold.font(size: 16))
                    .multilineTextAlignment(.center)
                    .padding(.top, 100)
                    .padding(.bottom, 40)
                HStack {
                    Spacer()
                    SecondaryButton(R.string.localizable.button_cancel.localizedKey, action: onCancel)
                    Spacer()
                    PrimaryButton(R.string.localizable.button_yes.localizedKey, action: onValid)
                    Spacer()
                }
                .padding(.bottom, 60)
            }
            .overlay {
                R.image.cancelGame.image
                    .padding(.bottom, 330)
            }
            .background(
                Color(UIColor.systemBackground)
                    .cornerRadius(16)
                    .shadow(color: R.color.onSurface.color.opacity(0.20),
                            radius: 4)
            )
        }
        .background(R.color.surface.color.opacity(0.7))
        .edgesIgnoringSafeArea(.bottom)
    }
}

#Preview {
    VStack {
        GamePlayCancelGameView(onCancel: {}, onValid: {})
    }
    .background(.red)
    .environmentObject(AppContext(username: "DEBUG"))
}
