//
//  OnBoardingPage4View.swift
//  CACDGAME
//
//  Created by François Dabonot on 11/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI

struct OnBoardingPage4View: View {

    @EnvironmentObject var appContext: AppContext
    @FocusState var focusedUsernameField: Bool

    @State var username: String = ""
    @State var inputError: LocalizedStringKey?

    var onNextPage: (String) -> Void
    var onPrevPage: () -> Void

    var isValidInput: Bool {
        username.count > 2 && inputError == nil
    }

    init(focusedUsernameField: FocusState<Bool>,
         onNextPage: @escaping (String) -> Void,
         onPrevPage: @escaping () -> Void) {
        self._focusedUsernameField = focusedUsernameField
        self.onNextPage = onNextPage
        self.onPrevPage = onPrevPage
    }

    var body: some View {
        VStack {
            CustomAttributedText(stringRes: R.string.localizable.onbaording_5_content.key,
                                 custom: [
                                    .DEFAULT: .init(font: R.font.poppinsMedium.font(size: 15)),
                                    .ITALIC: .init(font: R.font.poppinsBold.font(size: 15))
                                 ])
                .multilineTextAlignment(.leading)
                .fixedSize(horizontal: false, vertical: true)
                .padding(.top, 40)
                .padding(.bottom, 20)
                .padding([.leading, .trailing], 30)
            UsernameTextField(value: $username, inputError: inputError, withTitle: true)
                .focused($focusedUsernameField)
                .onSubmit {
                    guard isValidInput else { return }
                    onNextPage(username)
                }
                .submitLabel(.go)
                .padding()
            PrimaryButton(R.string.localizable.onbaording_5_button.localizedKey,
                          action: {
                            guard isValidInput else { return }
                            onNextPage(username)
                          })
                .disabled(!isValidInput)
                .padding(.top, 20)
            NavigationFooterView(onGoBack: onPrevPage, horizontalArrangement: .leading)
            Spacer()
        }
        .onAppear {
            username = appContext.username ?? ""
        }
        .onChange(of: username, perform: { newValue in
            if newValue.count > 10 {
                inputError = R.string.localizable.onboarding_5_error_max.localizedKey
            } else if newValue.count == 2 {
                inputError = R.string.localizable.onboarding_5_error_min.localizedKey
            } else {
                inputError = nil
            }
        })
        .modifier(CommonCardBackground())
    }
}

#Preview {
    OnBoardingPage4View(focusedUsernameField: .init(), onNextPage: {_ in }, onPrevPage: {})
        .preferredColorScheme(.light)
        .environmentObject(AppContext())
}

#Preview {
    OnBoardingPage4View(focusedUsernameField: .init(), onNextPage: {_ in }, onPrevPage: {})
        .preferredColorScheme(.dark)
        .environmentObject(AppContext())
}
