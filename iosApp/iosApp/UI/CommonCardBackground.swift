//
//  CommonCardBackgroundView.swift
//  CACDGAME
//
//  Created by François Dabonot on 10/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI

struct CommonCardBackground: ViewModifier {

    let padding: CGFloat

    init(padding: CGFloat = 8) {
        self.padding = padding
    }

    func body(content: Content) -> some View {
        /*ZStack {
         Color(UIColor.systemBackground)
         .cornerRadius(16)
         .background(
         RoundedCorners(background: R.color.surface.color,
         border: .clear,
         topLeft: 16,
         topRight: 16)
         .shadow(color: R.color.onSurface.color.opacity(0.15),
         radius: 6,
         x: 3,
         y: -3)
         )
         content
         }
         .padding(.top, padding)*/
        content
    }
}

#Preview {
    VStack {
        Text("DARK").frame(maxWidth: .infinity, maxHeight: .infinity)
    }.modifier(CommonCardBackground())
    .previewLayout(.fixed(width: 100, height: 200))
    .preferredColorScheme(.dark)

}
