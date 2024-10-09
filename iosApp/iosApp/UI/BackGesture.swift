//
//  BackGesture.swift
//  CACDGAME
//
//  Created by François Dabonot on 17/08/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI

struct BackGesture: ViewModifier {

    @EnvironmentObject var router: Router
    @GestureState private var dragOffset = CGSize.zero

    func body(content: Content) -> some View {
        content
            .highPriorityGesture(DragGesture(minimumDistance: 20, coordinateSpace: .global).onEnded { value in
                let horizontalAmount = value.translation.width as CGFloat
                let verticalAmount = value.translation.height as CGFloat

                if abs(horizontalAmount) > abs(verticalAmount) && horizontalAmount > 0 {
                    self.router.pop()
                }
            }
            )
    }
}

extension View {
    func hasBackGestion() -> some View {
        modifier(BackGesture())
    }
}
