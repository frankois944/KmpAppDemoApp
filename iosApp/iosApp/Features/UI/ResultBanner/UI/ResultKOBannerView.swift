//
//  ResultKOBannerView.swift
//  CACDGAME
//
//  Created by frankois on 13/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI

struct ResultKOBannerView: View {

    @EnvironmentObject var appContext: AppContext

    let message: String?

    var body: some View {
        VStack(alignment: .center) {
            HStack {
                Spacer()
                R.image.check_bad.image
                    .padding(.top, 10)
                    .padding(.trailing, 10)
                    .foregroundColor(R.color.error.color)
            }
            Text(message ?? R.string.localizable.game_banner_loose
                    .localizedKeyWithFormat(appContext.username ?? "DEBUG"))
                .multilineTextAlignment(.center)
                .font(R.font.poppinsSemiBold.font(size: 14))
                .foregroundColor(R.color.error.color)
                .padding(.bottom, 30)
                .padding(.trailing)
                .padding(.leading)
        }
        .background(
            RoundedCorners(background: R.color.error.color.opacity(0.12),
                           border: .clear,
                           topLeft: 4,
                           topRight: 4)
        )
    }
}

#Preview {
    VStack {
        Spacer()
        ResultKOBannerView(message: nil)
            .environmentObject(AppContext(username: "DEBUG"))
    }.previewLayout(.sizeThatFits)
}
