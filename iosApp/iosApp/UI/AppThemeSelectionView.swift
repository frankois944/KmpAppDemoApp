//
//  AppThemeSelectionView.swift
//  CACDGAME
//
//  Created by François Dabonot on 27/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import Shared

struct AppThemeSelectionView: View {

    @EnvironmentObject private var appContext: AppContext
    @State private var selection: ScreenMode = .system

    var body: some View {
        HStack {
            R.image.palette.image
                .foregroundColor(R.color.primary.color)
            Text(R.string.localizable.current_selected_theme.localizedKeyWithFormat(""))
                .foregroundColor(R.color.onSurface.color)
            Spacer()
            Picker("", selection: $selection) {
                Text(R.string.localizable.setting_theme_system.localizedKey).tag(ScreenMode.system)
                Text(R.string.localizable.setting_theme_light.localizedKey).tag(ScreenMode.light)
                Text(R.string.localizable.setting_theme_dark.localizedKey).tag(ScreenMode.dark)
            }
            .foregroundColor(R.color.primary.color)
        }
        .font(R.font.robotoRegular.font(size: 16))
        .pickerStyle(.menu)
        .onAppear {
            selection = appContext.screenMode
        }
        .onChange(of: selection) { newValue in
            switch newValue {
            case .system:
                trackEvent(name: "Change color theme for System")
            case .light:
                trackEvent(name: "Change color theme for Light")
            case .dark:
                trackEvent(name: "Change color theme for Dark")
            default:
                break
            }
            appContext.screenMode = newValue
        }
    }
}

#Preview {
    AppThemeSelectionView()
}
