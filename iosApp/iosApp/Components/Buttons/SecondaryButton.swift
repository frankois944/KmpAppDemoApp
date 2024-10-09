//
//  SecondaryButton.swift
//  CACDGAME
//
//  Created by François Dabonot on 10/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI

struct SecondaryButton: View {

    @Environment(\.isEnabled) private var isEnabled
    let title: String
    let localizedString: LocalizedStringKey?
    let action: () -> Void

    init(_ title: String, action: @escaping () -> Void) {
        self.title = title
        self.action = action
        self.localizedString = nil
    }

    init(_ title: LocalizedStringKey, action: @escaping () -> Void) {
        self.localizedString = title
        self.action = action
        self.title = ""
    }

    private var backgroundColor: Color {
        if isEnabled {
            return R.color.surface.color
        } else {
            return R.color.surface.color
        }
    }

    private var foregroundColor: Color {
        if isEnabled {
            return R.color.primary.color
        } else {
            return R.color.onSurface.color.opacity(0.38)
        }
    }

    var body: some View {
        Button(action: action) {
            if let localizedString = localizedString {
                Text(localizedString).tracking(1.25).textCase(.uppercase).lineSpacing(16)
                    .padding(EdgeInsets(top: 10, leading: 14, bottom: 10, trailing: 14))
            } else {
                Text(title).tracking(1.25).textCase(.uppercase).lineSpacing(16)
                    .padding(EdgeInsets(top: 10, leading: 14, bottom: 10, trailing: 14))
            }
        }
        .overlay(
            RoundedRectangle(cornerRadius: 50)
                .stroke(R.color.onSurface.color.opacity(0.12),
                        lineWidth: 1)
        )
        .font(R.font.robotoMedium.font(size: 14))
        .background(backgroundColor)
        .foregroundColor(foregroundColor)
    }
}

#Preview {
    VStack {
        SecondaryButton("Un test light") {
        }
        SecondaryButton("Un test dark") {
        }
        SecondaryButton("Un test dark disabled") {
        }
    }
}
