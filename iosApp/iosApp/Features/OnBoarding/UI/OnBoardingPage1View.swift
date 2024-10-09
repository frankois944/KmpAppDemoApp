//
//  OnBoardingPage1View.swift
//  CACDGAME
//
//  Created by François Dabonot on 06/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI

struct OnBoardingPage1View: View {

    var onNextPage: () -> Void

    var body: some View {
        VStack(alignment: .center) {
            R.image.onbaording1.image
                .padding()
            CustomAttributedText(stringRes: R.string.localizable.onboarding_1_content.key)
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

    NavigationStack {
        OnBoardingPage1View {
        }
        .toolbar {
            ToolbarItem(placement: .navigationBarLeading) {
                R.image.small_logo.image
            }
        }
    }
    .preferredColorScheme(.light)
    .toolbar(.visible, for: .navigationBar)
    .environmentObject(AppContext())
}

#Preview {

    NavigationStack {
        OnBoardingPage1View {
        }
        .toolbar {
            ToolbarItem(placement: .navigationBarLeading) {
                R.image.small_logo.image
            }
        }
    }
    .preferredColorScheme(.dark)
    .toolbar(.visible, for: .navigationBar)
    .environmentObject(AppContext())

}
