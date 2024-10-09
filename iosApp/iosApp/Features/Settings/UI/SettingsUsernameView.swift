//
//  SettingsUsernameView.swift
//  CACDGAME
//
//  Created by François Dabonot on 27/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI

struct SettingsUsernameView: View {

    @EnvironmentObject var appContext: AppContext

    @FocusState private var focusedField: Bool
    @State private var username: String = ""
    @State private var inputError: LocalizedStringKey?

    private func getInputError(value: String) -> LocalizedStringKey? {
        let trimmedValue = value.trimmingCharacters(in: .whitespacesAndNewlines)
        if trimmedValue.count > 10 {
            return R.string.localizable.onboarding_5_error_max.localizedKey
        } else if trimmedValue.count <= 2 {
            return R.string.localizable.onboarding_5_error_min.localizedKey
        } else {
            return nil
        }
    }

    var body: some View {
        VStack {
            HStack {
                R.image.person.image
                    .foregroundColor(R.color.primary.color)
                Text(R.string.localizable.setting_username_label.localizedKey)
                    .font(R.font.poppinsExtraBold.font(size: 12))
                    .foregroundColor(R.color.primary.color)
                Spacer()
            }
            UsernameTextField(value: $username,
                              inputError: inputError,
                              withTitle: false,
                              footer: R.string.localizable.setting_can_edit_username.string)
                .focused($focusedField)
                .submitLabel(.done)
                .onSubmit {
                    guard getInputError(value: username) == nil else { return }
                    focusedField = false
                    appContext.username = username
                }
            HStack {
                Spacer()
                PrimaryButton(R.string.localizable.username_update.localizedKey) {
                    guard getInputError(value: username) == nil else { return }
                    focusedField = false
                    appContext.username = username
                }
                .disabled(getInputError(value: username) != nil ||
                            username.trimmingCharacters(in: .whitespacesAndNewlines) == appContext.username)
            }
        }
        .onChange(of: username, perform: { newValue in
            inputError = getInputError(value: newValue)
        })
        .onAppear {
            username = appContext.username ?? ""
        }
    }
}

#Preview {
    SettingsUsernameView()
        .environmentObject(AppContext())
}
