//
//  OnBoardingPage2View.swift
//  CACDGAME
//
//  Created by François Dabonot on 06/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI

struct OnBoardingPage2View: View {

    var onNextPage: () -> Void
    var onPrevPage: () -> Void

    var body: some View {
        VStack(alignment: .center) {
            R.image.onbaording2.image
                .padding()
            CustomAttributedText(stringRes: R.string.localizable.onboarding_2_content.key)
                .padding()
                .multilineTextAlignment(.leading)
                .fixedSize(horizontal: false, vertical: true)
            Spacer()
            PrimaryButton(R.string.localizable.onboarding_next_page.localizedKey,
                          action: onNextPage)
            Spacer()
        }
        .frame(maxWidth: .infinity)
        .padding()
        .scrollOnOverflow()
    }
}

#Preview {
    OnBoardingPage2View(onNextPage: {}, onPrevPage: {})
        .environmentObject(AppContext())
}

#Preview {
    OnBoardingPage2View(onNextPage: {}, onPrevPage: {})
        .environmentObject(AppContext())
        .preferredColorScheme(.dark)
}
