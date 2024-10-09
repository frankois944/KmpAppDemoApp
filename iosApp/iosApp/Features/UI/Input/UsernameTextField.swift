//
//  UsernameTextField.swift
//  CACDGAME
//
//  Created by François Dabonot on 27/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import SwiftUIIntrospect

struct UsernameTextField: View {

    @Binding var value: String
    @State var isFocused = false
    @State private var customTextColor = R.color.onSurface.color
    @State private var placeHolderColor = R.color.onSurface.color.opacity(0.60)
    @State private var customBorderColor = R.color.onSurface.color.opacity(0.38)
    @State private var iconColor = R.color.onSurface.color.opacity(0.54)
    let footer: String?
    let inputError: LocalizedStringKey?
    let withTitle: Bool

    init(value: Binding<String>,
         inputError: LocalizedStringKey?,
         withTitle: Bool,
         footer: String? = R.string.localizable.onboarding_5_tips.string) {
        self._value = value
        self.inputError = inputError
        self.withTitle = withTitle
        self.footer = footer
    }

    private func customize() {
        if isFocused {
            customTextColor = R.color.primary.color
            placeHolderColor = R.color.primary.color
        } else {
            customTextColor = R.color.onSurface.color
            placeHolderColor =  R.color.onSurface.color.opacity(0.60)
        }
        if inputError != nil {
            customBorderColor = R.color.error.color
        } else {
            if isFocused {
                customBorderColor = R.color.primary.color
            } else {
                customBorderColor = R.color.onSurface.color.opacity(0.38)
            }
        }
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            if withTitle {
                Text(R.string.localizable.setting_username_label.localizedKey)
                    .padding(.trailing, 16)
                    .padding(.leading, 16)
                    .foregroundColor(inputError != nil ? R.color.error.color : placeHolderColor)
                    .font(R.font.robotoRegular.font(size: 16))
            }
            HStack {
                TextField(R.string.localizable.onboarding_5_placeholder.localizedKey,
                          text: $value,
                          prompt:
                            Text(R.string.localizable.onboarding_5_placeholder.localizedKey)
                            .foregroundColor(R.color.onSurface.color.opacity(0.74))
                )
                .introspect(.textField, on: .iOS(.v16, .v17)) { txt in
                    DispatchQueue.main.async {
                        self.isFocused = txt.isEditing
                    }
                }
                R.image.editing.image
                    .foregroundColor(inputError != nil ? R.color.error.color : iconColor)
            }
            .modifier(
                CustomViewModifier2(roundedCornes: 4,
                                    textColor: R.color.onSurface.color,
                                    borderColor: inputError != nil ? R.color.error.color : customBorderColor,
                                    lineWidth: isFocused ? 2 : 1)
            )
            if let inputError = inputError {
                Text(inputError).padding(.trailing, 16)
                    .padding(.leading, 16)
                    .foregroundColor(R.color.error.color)
                    .font(R.font.robotoRegular.font(size: 12))
            } else if let footer = footer {
                Text(footer).padding(.trailing, 16)
                    .padding(.leading, 16)
                    .foregroundColor(placeHolderColor)
                    .font(R.font.robotoRegular.font(size: 12))
            }
        }
        .onChange(of: isFocused) { _ in
            customize()
        }
    }
}

struct CustomViewModifier2: ViewModifier {
    let roundedCornes: CGFloat
    let textColor: Color
    let borderColor: Color
    let lineWidth: Double

    init(roundedCornes: CGFloat, textColor: Color, borderColor: Color, lineWidth: Double = 1.0) {
        self.roundedCornes = roundedCornes
        self.textColor = textColor
        self.borderColor = borderColor
        self.lineWidth = lineWidth
    }

    func body(content: Content) -> some View {
        content
            .padding()
            .border(borderColor)
            .cornerRadius(roundedCornes)
            .foregroundColor(textColor)
            .font(R.font.robotoRegular.font(size: 16))
            .background(RoundedCorners(background: .clear, border: borderColor,
                                       topLeft: roundedCornes,
                                       topRight: roundedCornes,
                                       bottomLeft: roundedCornes,
                                       bottomRight: roundedCornes,
                                       lineWidth: lineWidth))
    }
}

#Preview {
    VStack {
        UsernameTextField(value: .constant(""), inputError: nil, withTitle: true, footer: "treet")
        UsernameTextField(value: .constant("123423"), inputError: nil, withTitle: true)
        UsernameTextField(value: .constant(""), inputError: "5242212", withTitle: true)
    }
    .environmentObject(AppContext())
    .padding()

}
