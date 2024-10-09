//
//  SettingsScreen.swift
//  CACDGAME
//
//  Created by François Dabonot on 06/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI

struct SettingsScreen: View {

    static let route = SettingsModel.self

    @EnvironmentObject var appContext: AppContext
    @EnvironmentObject var router: Router
    @State var displayConsentManager: Bool = false
    @State var displayOnboarding: Bool = false

    var body: some View {
        VStack {
            SettingsUsernameView()
            if !Bundle.main.isProduction {
                Button("ONBOARDING TESTING") {
                    displayOnboarding = true
                }
            }
            LanguageSelectionView()
                .frame(height: 56)
            Divider()
            AppThemeSelectionView()
                .frame(height: 56)
            Divider()
            HStack {
                R.image.privacy.image
                    .foregroundColor(R.color.primary.color)
                Text(R.string.localizable.privacy_setting_button.localizedKey)
                    .foregroundColor(R.color.onSurface.color)
                Spacer()
                R.image.chevron_right.image
                    .foregroundColor(R.color.onSurface.color)
            }
            .frame(height: 56)
            .contentShape(Rectangle())
            .onTapGesture {
                displayConsentManager = true
            }
            Divider()
        }
        .frame(maxHeight: .infinity, alignment: .top)
        .padding()
        .modifier(CommonCardBackground())
        .sheet(isPresented: $displayConsentManager, content: {
            ConsentManagerScreen()
                .mpresentationBackground(.clear)
                .analyticsScreen(name: "ConsentManagerFromSetting",
                                 class: "ConsentManagerFromSetting")
        })
        .fullScreenCover(isPresented: $displayOnboarding) {
            OnBoardingScreen {
                appContext.username = $0
                displayOnboarding = false
            }
        }
    }
}

#Preview {
    SettingsScreen()
        .environmentObject(AppContext())
}
