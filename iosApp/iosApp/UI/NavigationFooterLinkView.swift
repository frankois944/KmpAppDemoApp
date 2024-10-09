//
//  NavigationFooterLinkView.swift
//  CACDGAME
//
//  Created by François Dabonot on 12/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI

struct NavigationFooterLinkView: View {

    @EnvironmentObject var router: Router

    let canGoForward: Bool
    let canGoBackward: Bool
    let horizontalArrangement: Alignment
    let value: Router.Route

    init(for value: Router.Route,
         canGoForward: Bool = true,
         canGoBackward: Bool = false,
         horizontalArrangement: Alignment = .trailing) {
        self.value = value
        self.canGoForward = canGoForward
        self.canGoBackward = canGoBackward
        self.horizontalArrangement = horizontalArrangement
    }

    var body: some View {
        HStack(alignment: .center) {
            if canGoBackward {
                Button {
                    router.pop()
                } label: {
                    R.image.arrowBackward.image
                }.padding(.leading, 24)
            }
            if canGoForward {
                Button {
                    router.push(route: value)
                } label: {
                    R.image.arrowForward.image
                        .foregroundColor(R.color.primary.color)
                }
                .padding(.trailing, 40)
                .padding(.leading, 40)
            }
        }
        .frame(maxWidth: .infinity, alignment: horizontalArrangement)
        .frame(height: 50)
    }
}

#Preview {
    NavigationFooterLinkView(for: .contact(model: .init()))
        .environmentObject(AppContext())
        .environmentObject(Router())
}
