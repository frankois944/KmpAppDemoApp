//
//  LargeTitleStyle.swift
//  testTheming
//
//  Created by François Dabonot on 20/04/2023.
//

import Foundation
import SwiftUI

struct LargeTitleStyle: ViewModifier {

    @EnvironmentObject var theme: AppTheme

    let defaultTextColor: Color?

    init(defaultTextColor: Color? = nil) {
        self.defaultTextColor = defaultTextColor
    }

    func body(content: Content) -> some View {
        content.font(theme.typography.h2)
            .foregroundColor(defaultTextColor ?? theme.colors.onSurface)
            .lineSpacing(16)
            .multilineTextAlignment(.leading)
    }
}
