//
//  ContactScreen.swift
//  CACDGAME
//
//  Created by François Dabonot on 06/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import Shared
import Lottie

enum ContactField: Int {
    case firstname = 0, lastname = 1, email = 2, phone = 3, message = 4

    var label: LocalizedStringKey {
        switch self {
        case .firstname:
            return R.string.localizable.contact_firstname.localizedKey
        case .lastname:
            return R.string.localizable.contact_lastname.localizedKey
        case .email:
            return R.string.localizable.contact_email.localizedKey
        case .phone:
            return R.string.localizable.contact_phone.localizedKey
        case .message:
            return R.string.localizable.contact_message.localizedKey
        }
    }

    static func isEmailValid(_ input: String) -> Bool {
        let emailPattern = Shared.Constants.shared.EMAIL_REGEX
        return input.range(
            of: emailPattern,
            options: .regularExpression
        ) != nil
    }
}

struct ContactScreen: View {

    static let route = ContactModel.self

    @FocusState var focusedField: ContactField?
    @State var savingState: UIState<Void>?
    @State var triggerSendForm: Bool = false
    @State var firstNameError: LocalizedStringKey?
    @State var firstname: String = ""
    @State var lastnameError: LocalizedStringKey?
    @State var lastname: String = ""
    @State var emailError: LocalizedStringKey?
    @State var email: String = ""
    @State var phone: String = ""
    @State var messageError: LocalizedStringKey?
    @State var message: String = ""
    @State var check: Bool = false
    @State var displayResultBanner: Bool?

    func clearFields() {
        firstNameError = nil
        firstname = ""
        lastnameError = nil
        lastname = ""
        emailError = nil
        email = ""
        phone = ""
        messageError = nil
        message = ""
        check = false
    }

    func checkAndSendForm() {
        if firstname.isEmpty || firstname.count > 100 {
            firstNameError = R.string.localizable.mandatory_field.localizedKey
        } else {
            firstNameError = nil
        }
        if lastname.isEmpty || lastname.count > 100 {
            lastnameError = R.string.localizable.mandatory_field.localizedKey
        } else {
            lastnameError = nil
        }
        if email.isEmpty || email.count > 100 || !ContactField.isEmailValid(email) {
            emailError = R.string.localizable.mandatory_field.localizedKey
        } else {
            emailError = nil
        }
        if message.isEmpty || message.count > 100 {
            messageError = R.string.localizable.mandatory_message.localizedKey
        } else {
            messageError = nil
        }
        let hasError = !check ||
            firstNameError != nil ||
            lastnameError != nil ||
            emailError != nil ||
            messageError != nil
        if !hasError {
            savingState = .loading
            Task { @MainActor in
                do {
                    let isOk = try await Contact.shared.createLead(firstname: firstname,
                                                                   lastname: lastname,
                                                                   email: email,
                                                                   comment: message,
                                                                   phoneNumber: phone)
                    if isOk.boolValue {
                        savingState = .success(())
                    } else {
                        savingState = .error(KotlinException(message: "Generic error").asError())
                    }
                } catch {
                    AppLogger.shared.e(messageString: "Create contact error", throwable: error)
                }
                displayResultBanner = true
            }
        }
    }

    var body: some View {
        ZStack(alignment: .bottom) {
            ScrollViewReader { reader in
                ScrollView {
                    PrimaryTextField(title: R.string.localizable.contact_firstname.string,
                                     value: $firstname,
                                     localizedError: firstNameError)
                        .focused($focusedField, equals: .firstname)
                        .submitLabel(.next)
                        .textContentType(.givenName)
                        .padding([.top, .leading, .trailing])
                        .id(ContactField.firstname)

                    PrimaryTextField(title: R.string.localizable.contact_lastname.string,
                                     value: $lastname,
                                     localizedError: lastnameError)
                        .focused($focusedField, equals: .lastname)
                        .submitLabel(.next)
                        .textContentType(.familyName)
                        .padding([.top, .leading, .trailing])
                        .id(ContactField.lastname)

                    PrimaryTextField(title: R.string.localizable.contact_email.string,
                                     value: $email,
                                     localizedError: emailError)
                        .focused($focusedField, equals: .email)
                        .submitLabel(.next)
                        .keyboardType(.emailAddress)
                        .textContentType(.emailAddress)
                        .padding([.top, .leading, .trailing])
                        .id(ContactField.email)

                    PrimaryTextField(title: R.string.localizable.contact_phone.string,
                                     value: $phone)
                        .focused($focusedField, equals: .phone)
                        .submitLabel(.next)
                        .keyboardType(.phonePad)
                        .textContentType(.telephoneNumber)
                        .padding([.top, .leading, .trailing])
                        .id(ContactField.phone)

                    PrimaryTextField(title: R.string.localizable.contact_message.string,
                                     value: $message,
                                     localizedError: messageError,
                                     multiLines: true)
                        .focused($focusedField, equals: .message)
                        .submitLabel(.return)
                        .padding([.top, .bottom, .leading, .trailing])
                        .id(ContactField.message)

                    Toggle(isOn: $check) {
                        Text(R.string.localizable.contact_require.localizedKey)
                            .font(R.font.robotoRegular.font(size: 15))
                            .foregroundColor(R.color.onSurface.color)
                            .fixedSize(horizontal: false, vertical: true)
                    }
                    .padding([.leading, .trailing])
                    if let savingState = savingState {
                        switch savingState {
                        case .loading:
                            LottieView(animation: LottieAnimation.named(R.file.loadingDotJson.name)!)
                                .resizable()
                                .looping()
                                .frame(height: 120)
                        default:
                            EmptyView()
                        }
                    } else {
                        PrimaryButton(R.string.localizable.contact_send.localizedKey) {
                            trackEvent(name: "Send Message")
                            checkAndSendForm()
                            focusedField = nil
                        }
                        .padding()
                        .disabled(!check)
                    }
                }
                .scrollDismissesKeyboard(.interactively)
                .onSubmit {
                    switch focusedField {
                    case .firstname:
                        focusedField = .lastname
                    case .lastname:
                        focusedField = .email
                    case .email:
                        focusedField = .phone
                    case .phone:
                        focusedField = .message
                    default:
                        focusedField = nil
                    }
                }
                .onChange(of: focusedField, perform: { item in
                    DispatchQueue.main.async {
                        reader.scrollTo(item, anchor: .bottom)
                    }
                })
                .onChange(of: displayResultBanner, perform: { _ in
                    if displayResultBanner == false {
                        if case .success(()) = savingState {
                            clearFields()
                        }
                        savingState = nil
                    }
                })
                .onDisappear(perform: {
                    savingState = nil
                })
                // .padding()
                .toolbar {
                    ToolbarItem(placement: .keyboard) {
                        HStack {
                            Button(R.string.localizable.done_title.localizedKey) {
                                focusedField = nil
                            }
                            Spacer()
                            if let label = focusedField?.label {
                                Text(label).font(R.font.robotoRegular.font(size: 16))
                            }
                            Spacer()
                            if focusedField == .phone {
                                Button(R.string.localizable.next_label.localizedKey) {
                                    focusedField = .message
                                }
                            }
                        }
                    }
                }
            }
            switch savingState {
            case .success:
                ContactSentView(isDisplayed: $displayResultBanner)
                    .onTapGesture {
                        displayResultBanner = false
                    }
            case .error:
                ResultBannerView(
                    isValid: false,
                    message: R.string.localizable.generic_error.string,
                    isDisplayed: $displayResultBanner
                ).onTapGesture {
                    displayResultBanner = false
                }
            default:
                EmptyView()
            }
        }
    }
}

#Preview {
    ContactScreen()
        .environmentObject(AppContext())
}
