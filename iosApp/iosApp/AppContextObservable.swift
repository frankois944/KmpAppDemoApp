//
//  AppContextStates.swift
//  CACDGAME
//
//  Created by François Dabonot on 03/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import Foundation
import SwiftUI
import Shared

class AppContext: ObservableObject {

    static let shared = AppContext()

    private static func currentContext() -> Shared.AppContext? {
        Shared.AppContext.companion.context()
    }

    @Published var gameId: String?
    @Published var username: String? {
        didSet {
            Task {
                try? await save()
            }
        }
    }
    @Published var appbarTitle: String?
    @Published var forceHideBackButton: Bool
    @Published var forceHideBottomBar: Bool
    @Published var hasTimeout: Bool
    @Published var maxTimeout: TimeInterval
    @Published var showCloseScreenButton: Bool {
        didSet {
            Task {
                try? await save()
            }
        }
    }
    @Published var analyticsEnabled: Bool {
        didSet {
            Task {
                try? await save()
            }
        }
    }
    @Published var crashEnabled: Bool {
        didSet {
            Task {
                try? await save()
            }
        }
    }
    @Published var performanceEnabled: Bool {
        didSet {
            Task {
                try? await save()
            }
        }
    }
    @Published var screenMode: ScreenMode {
        didSet {
            Task {
                try? await save()
            }
        }
    }
    @Published var showSearchLexicalButton: Bool
    @Published var showAppBarContent: Bool
    @Published var forcedStatusBarColor: Color?
    @Published var isAppBarVisible: Bool
    @Published var isSearchCriteriaOpen: Bool
    @Published var currentLanguageCode: Language {
        didSet {
            PlatformKt.currentLanguage = currentLanguageCode
            Task {
                try? await save()
            }
        }
    }
    @Published var consentManagerDisplayed: Bool {
        didSet {
            Task {
                try? await save()
            }
        }
    }

    init(gameId: String? = nil,
         username: String? = nil,
         appbarTitle: String? = nil,
         forceHideBackButton: Bool = false,
         forceHideBottomBar: Bool = false,
         hasTimeout: Bool = true,
         maxTimeout: TimeInterval = TimeInterval(AppContextKt.defaultTimeout / 1000),
         showCloseScreenButton: Bool = false,
         analyticsEnabled: Bool = false,
         crashEnabled: Bool = false,
         performanceEnabled: Bool = false,
         screenMode: ScreenMode = .system,
         showSearchLexicalButton: Bool = false,
         showAppBarContent: Bool = false,
         forcedStatusBarColor: Color? = nil,
         isAppBarVisible: Bool = true,
         isSearchCriteriaOpen: Bool = false,
         currentLanguageCode: Language = Language.unknown,
         consentManagerDisplayed: Bool = false) {
        self.gameId = gameId
        self.username = username
        self.appbarTitle = appbarTitle
        self.forceHideBackButton = forceHideBackButton
        self.forceHideBottomBar = forceHideBottomBar
        self.hasTimeout = hasTimeout
        self.maxTimeout = maxTimeout
        self.showCloseScreenButton = showCloseScreenButton
        self.analyticsEnabled = analyticsEnabled
        self.crashEnabled = crashEnabled
        self.performanceEnabled = performanceEnabled
        self.screenMode = screenMode
        self.showSearchLexicalButton = showSearchLexicalButton
        self.showAppBarContent = showAppBarContent
        self.forcedStatusBarColor = forcedStatusBarColor
        self.isAppBarVisible = isAppBarVisible
        self.isSearchCriteriaOpen = isSearchCriteriaOpen
        self.currentLanguageCode = currentLanguageCode
        self.consentManagerDisplayed = consentManagerDisplayed
    }

    @MainActor
    func load() async throws {
        if let context = Shared.AppContext.companion.context() {
            try await context.loadContextFromDb()
            gameId = context.mGameId
            username = context.mUsername
            appbarTitle = context.mAppbarTitle
            forceHideBackButton = context.mForceHideBackButton?.boolValue ?? false
            forceHideBottomBar = context.mForceHideBottomBar?.boolValue ?? false
            hasTimeout = context.mHasTimeout?.boolValue ?? true
            maxTimeout = (context.mMaxTimeout?.doubleValue ?? TimeInterval(AppContextKt.defaultTimeout)) / 1000
            showCloseScreenButton = context.mShowCloseScreenButton?.boolValue ?? false
            analyticsEnabled = context.mAnalyticsEnabled?.boolValue ?? false
            crashEnabled = context.mCrashEnabled?.boolValue ?? false
            performanceEnabled = context.mPerformanceEnabled?.boolValue ?? false
            screenMode = context.mScreenMode ?? .system
            showSearchLexicalButton = context.mShowHideScreenButton?.boolValue ?? false
            showAppBarContent = context.mShowAppBarContent?.boolValue ?? false
            //            forcedStatusBarColor = context.mForcedStatusBarColor?.intValue
            isAppBarVisible = context.mIsAppBarVisible?.boolValue ?? true
            isSearchCriteriaOpen = context.mIsSearchCriteriaOpen?.boolValue ?? false
            currentLanguageCode = Language.companion.fromCode(code: context.mCurrentLanguageCode ?? "?")
            consentManagerDisplayed = context.mConsentManagerDisplayed?.boolValue ?? false
        }
    }

    @MainActor
    func save() async throws {
        if let context = Shared.AppContext.companion.context() {
            try await context.storeContext(username: username,
                                           hasTimeout: KotlinBoolean(bool: hasTimeout),
                                           analyticsEnabled: KotlinBoolean(bool: analyticsEnabled),
                                           crashEnabled: KotlinBoolean(bool: crashEnabled),
                                           performanceEnabled: KotlinBoolean(bool: performanceEnabled),
                                           screenMode: screenMode,
                                           languageCode: currentLanguageCode.code,
                                           consentManagerDisplayed: KotlinBoolean(bool: consentManagerDisplayed))
        }
    }
}
