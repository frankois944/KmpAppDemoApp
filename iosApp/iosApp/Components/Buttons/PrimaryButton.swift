//
//  PrimaryButton.swift
//  CACDGAME
//
//  Created by François Dabonot on 10/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI

struct PrimaryButton: View {

    @Environment(\.isEnabled) private var isEnabled
    let title: String
    let localizedString: LocalizedStringKey?
    let action: () -> Void
    let leftImageName: String?
    let rightImageName: String?

    init(_ title: String,
         leftImageName: String? = nil,
         rightImageName: String? = nil,
         action: @escaping () -> Void) {
        self.title = title
        self.action = action
        self.localizedString = nil
        self.leftImageName = leftImageName
        self.rightImageName = rightImageName
    }

    init(_ title: LocalizedStringKey,
         leftImageName: String? = nil,
         rightImageName: String? = nil,
         action: @escaping () -> Void) {
        self.localizedString = title
        self.action = action
        self.title = ""
        self.leftImageName = leftImageName
        self.rightImageName = rightImageName
    }

    private var backgroundColor: Color {
        if isEnabled {
            return R.color.primary.color
        } else {
            return R.color.onSurface.color.opacity(0.12)
        }
    }

    private var foregroundColor: Color {
        if isEnabled {
            return R.color.onPrimary.color
        } else {
            return R.color.onSurface.color.opacity(0.38)
        }
    }

    var body: some View {
        Button(action: action) {
            if let leftImageName = leftImageName {
                Image(leftImageName).padding(.trailing, 5)
            }
            if let localizedString = localizedString {
                Text(localizedString)
                    .tracking(1.25)
                    .textCase(.uppercase)
            } else {
                Text(title)
                    .tracking(1.25)
                    .textCase(.uppercase)
            }
            if let rightImageName = rightImageName {
                Image(rightImageName).padding(.leading, 5)
            }
        }
        .font(R.font.robotoMedium.font(size: 14))
        .lineSpacing(16)
        .padding(EdgeInsets(top: 10, leading: 14, bottom: 10, trailing: 14))
        .frame(height: 36)
        .frame(maxHeight: 36)
        .background(backgroundColor)
        .foregroundColor(foregroundColor)
        .clipShape(Capsule())
    }
}

#Preview {
    VStack {
        PrimaryButton("bouton") {
        }
        PrimaryButton("bouton", leftImageName: R.image.close.name) {
        }
        PrimaryButton("bouton", rightImageName: R.image.close.name) {
        }
        PrimaryButton("bouton Disabled") {
        }.disabled(true)
    }
}
