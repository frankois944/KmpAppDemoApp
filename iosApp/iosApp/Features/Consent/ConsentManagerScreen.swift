//
//  ConsentManagerScreen.swift
//  CACDGAME
//
//  Created by François Dabonot on 06/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import Shared

struct ConsentManagerScreen: View {

    static let route = ConsentManagerModel.self

    @EnvironmentObject var appContext: AppContext
    @EnvironmentObject var router: Router
    @Environment(\.openURL) var openURL
    @Environment(\.dismiss) var dismiss
    @State var analytics: Bool = false
    @State var crash: Bool = false
    @State var performance: Bool = false
    @State var data = ConsentManagerData(analytics: true, crash: true, performance: true)

    private func updateConsent(data: ConsentManagerData) async {
        SharedApp.shared.updateAnalytics(consent: data)
        _ = await Task {
            appContext.crashEnabled = data.crash
            appContext.analyticsEnabled = data.analytics
            appContext.performanceEnabled = data.performance
        }.value
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            Spacer()
            R.image.cookie.image.padding(.leading, 33)
            VStack(alignment: .leading) {
                VStack(alignment: .leading, spacing: 18) {
                    LanguageSelectionView(leftIcnSpace: 16)
                    Button(R.string.localizable.consent_private.string) {
                        openURL(URL(string: "https://www.cacd2.fr/mobile-privacy.html")!)
                    }
                    .font(R.font.robotoMedium.font(size: 12))
                }
                VStack(alignment: .leading) {
                    VStack(alignment: .leading, spacing: 6) {
                        Text(R.string.localizable.consent_title.string)
                            .font(R.font.poppinsBold.font(size: 15))
                            .foregroundColor(R.color.primary.color)
                        Text(R.string.localizable.consent_description.string)
                            .font(R.font.poppinsLight.font(size: 15))
                            .fixedSize(horizontal: false, vertical: true)
                    }
                    .padding(.top, 20)
                    .padding(.bottom, 20)
                    Toggle(R.string.localizable.consent_analytics.string, isOn: $analytics)
                        .font(R.font.poppinsBold.font(size: 15))
                    Toggle(R.string.localizable.consent_crash.string, isOn: $crash)
                        .font(R.font.poppinsBold.font(size: 15))
                    Toggle(R.string.localizable.consent_performance.string, isOn: $performance)
                        .font(R.font.poppinsBold.font(size: 15))
                    VStack(alignment: .leading, spacing: 20) {
                        PrimaryButton(R.string.localizable.consent_take_all.string) {
                            Task { @MainActor in
                                await updateConsent(data: .init(analytics: true, crash: true, performance: true))
                                dismiss()
                            }
                        }
                        SecondaryButton(R.string.localizable.consent_take_selected.string) {
                            Task { @MainActor in
                                await updateConsent(data: data)
                                dismiss()
                            }
                        }
                        SecondaryButton(R.string.localizable.consent_refuse_all.string) {
                            Task { @MainActor in
                                await updateConsent(data: .init(analytics: false, crash: false, performance: false))
                                dismiss()
                            }
                        }
                    }
                    .padding()
                }.onChange(of: analytics) { newValue in
                    data.analytics = newValue
                }.onChange(of: crash) { newValue in
                    data.crash = newValue
                }.onChange(of: performance) { newValue in
                    data.performance = newValue
                }
            }
            .padding()
            .background(R.color.background.color)
            .onAppear {
                analytics = appContext.analyticsEnabled
                crash = appContext.crashEnabled
                performance = appContext.performanceEnabled
            }
            .cornerRadius(16)
        }
        .onAppear(perform: {
            data = ConsentManagerData(analytics: appContext.analyticsEnabled,
                                      crash: appContext.crashEnabled,
                                      performance: appContext.performanceEnabled)
        })
        .scrollOnOverflow()
    }
}

#Preview {
    ConsentManagerScreen()
        .environmentObject(AppContext())
        .environmentObject(Router())
}
