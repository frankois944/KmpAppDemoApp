//
//  MyApplicationTheme.swift
//  iosApp
//
//  Created by François Dabonot on 19/04/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import Shared

class ThemeManager: ObservableObject {

    static let shared = ThemeManager()

    private var mode: ScreenMode = .system

    func update(screenMode: ScreenMode) {
        AppContext.shared.screenMode = screenMode
    }
}
