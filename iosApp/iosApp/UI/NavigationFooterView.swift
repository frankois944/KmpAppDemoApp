//
//  NavigationFooterView.swift
//  CACDGAME
//
//  Created by François Dabonot on 10/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI

struct NavigationFooterView: View {

    let onGoForward: (() -> Void)?
    let onGoBack: (() -> Void)?
    let horizontalArrangement: Alignment

    init(onGoForward: (() -> Void)? = nil,
         onGoBack: (() -> Void)? = nil,
         horizontalArrangement: Alignment = .trailing) {
        self.onGoForward = onGoForward
        self.onGoBack = onGoBack
        self.horizontalArrangement = horizontalArrangement
    }

    var body: some View {
        HStack(alignment: .center) {
            if let onGoBack = onGoBack {
                Button {
                    onGoBack()
                } label: {
                    R.image.arrowBackward.image
                }.padding(.leading, 24)
            }
            if let onGoForward = onGoForward {
                Button {
                    onGoForward()
                } label: {
                    R.image.arrowForward.image
                        .foregroundColor(R.color.primary.color)
                }
                .padding(.trailing, 40)
                .padding(.leading, 40)
            }
        }
        .frame(maxWidth: .infinity, alignment: horizontalArrangement)
    }
}

#Preview {
    NavigationFooterView(onGoForward: {}, onGoBack: {})
        .environmentObject(AppContext())
}
