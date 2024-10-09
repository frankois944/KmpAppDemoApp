//
//  ResultOKBannerView.swift
//  CACDGAME
//
//  Created by frankois on 13/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI

struct ResultOKBannerView: View {

    @EnvironmentObject var appContext: AppContext

    let message: String?

    var body: some View {
        VStack(alignment: .center) {
            HStack {
                Spacer()
                R.image.check_bad.image
                    .padding(.top, 10)
                    .padding(.trailing, 10)
                    .foregroundColor(R.color.onSecondary.color)
            }
            Text(message ?? R.string.localizable.game_banner_win
                    .localizedKeyWithFormat(appContext.username ?? "DEBUG"))
                .multilineTextAlignment(.center)
                .font(R.font.poppinsSemiBold.font(size: 14))
                .foregroundColor(R.color.onSecondary.color)
                .padding(.bottom, 30)
                .padding(.leading)
                .padding(.trailing)

        }
        .background(
            RoundedCorners(background: R.color.secondary.color,
                           border: .clear,
                           topLeft: 4,
                           topRight: 4)
        )
    }
}

#Preview {
    VStack {
        Spacer()
        ResultOKBannerView(message: nil)
            .environmentObject(AppContext(username: "DEBUG"))
    }.previewLayout(.sizeThatFits)
}
