//
//  PrimaryTextField.swift
//  CACDGAME
//
//  Created by François Dabonot on 20/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI

struct PrimaryTextField: View {

    @FocusState private var isFocused: Bool
    @State private var customTextColor = R.color.onSurface.color
    @State private var customBorderColor = R.color.onSurface.color.opacity(0.38)
    @State private var placeHolderColor = R.color.onSurface.color.opacity(0.60)
    @State private var iconColor = R.color.onSurface.color.opacity(0.54)
    @Binding var value: String
    let error: String?
    let localizedError: LocalizedStringKey?
    let title: String
    let placeholder: String?
    let footer: String?
    let rightImage: String?
    let multiLines: Bool

    init(title: String,
         placeholder: String? = nil,
         value: Binding<String>,
         error: String? = nil,
         localizedError: LocalizedStringKey? = nil,
         footer: String? = nil,
         rightImage: String? = nil,
         multiLines: Bool = false
    ) {
        self.title = title
        self._value = value
        self.error = error
        self.localizedError = localizedError
        self.placeholder = placeholder
        self.footer = footer
        self.rightImage = rightImage
        self.multiLines = multiLines
    }

    private func customize() {
        if isFocused {
            customTextColor = R.color.primary.color
            placeHolderColor = R.color.primary.color
        } else {
            customTextColor = R.color.onSurface.color
            placeHolderColor =  R.color.onSurface.color.opacity(0.38)
        }
        if isFocused {
            customBorderColor = isError ? R.color.error.color : R.color.primary.color
        } else {
            customBorderColor = isError ? R.color.error.color : R.color.onSurface.color.opacity(0.38)
        }
    }

    var isError: Bool {
        error != nil || localizedError != nil
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            HStack {
                if multiLines {
                    TextField(title,
                              text: $value,
                              prompt: Text(title)
                                .font(R.font.robotoRegular.font(size: 16))
                                .foregroundColor(R.color.onSurface.color.opacity(0.74)),
                              axis: .vertical)
                        .lineLimit(3...3)
                        .focused($isFocused)
                        .onChange(of: isFocused) { _ in
                            customize()
                        }
                } else {
                    TextField(title,
                              text: $value,
                              prompt: Text(title)
                                .font(R.font.robotoRegular.font(size: 16))
                                .foregroundColor(R.color.onSurface.color.opacity(0.74))

                    )
                    .focused($isFocused)
                    .onChange(of: isFocused) { _ in
                        customize()
                    }
                }
                if let rightImage = rightImage {
                    Image(rightImage)
                        .foregroundColor(isError ? R.color.error.color : iconColor)
                }
            }
            .modifier(
                CustomViewModifier(roundedCornes: 4,
                                   textColor: R.color.onSurface.color,
                                   borderColor: isError ? R.color.error.color : customBorderColor,
                                   lineWidth: isFocused ? 2 : 1)
            )
            if let localizedError = localizedError {
                Text(localizedError)
                    .foregroundColor(R.color.error.color)
                    .font(R.font.robotoRegular.font(size: 12))
            } else if let inputError = error {
                Text(inputError)
                    .foregroundColor(R.color.error.color)
                    .font(R.font.robotoRegular.font(size: 12))
            } else if let footer = footer {
                Text(footer)
                    .foregroundColor(placeHolderColor)
                    .font(R.font.robotoRegular.font(size: 12))
            }
        }
    }
}

struct CustomViewModifier: ViewModifier {
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
            .background(RoundedCorners(background: .clear,
                                       border: borderColor,
                                       topLeft: roundedCornes,
                                       topRight: roundedCornes,
                                       bottomLeft: roundedCornes,
                                       bottomRight: roundedCornes,
                                       lineWidth: lineWidth))
    }
}

#Preview {
    VStack {
        PrimaryTextField(title: "dsffd", value: .constant(""))
    }
}
