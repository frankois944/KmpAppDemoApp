import SwiftUI
import Shared
import Nuke
import Shake

var currentLocal: Locale {
    let currentRegion = Locale.current.language.region?.identifier ?? "us"
    return .init(identifier: "\(AppContext.shared.currentLanguageCode.code)_\(currentRegion)")
}

@main
struct MainApp: App {

    @Environment(\.colorScheme) var systemColorScheme
    @State var customColorScheme: ColorScheme?
    @StateObject var theme = ThemeManager.shared
    @StateObject var appContext = AppContext.shared
    @State var isLoaded = false

    init() {
        ImagePipeline.shared = ImagePipeline(configuration: .withDataCache)
        #if DEBUG
        PlatformKt.isDebug = true
        #endif
        PlatformKt.isTesting = !Bundle.main.isProduction
        if !Bundle.main.isProduction {
            if !Bundle.main.isPreview {
                Shake.configuration.isFloatingReportButtonShown = true
                Shake.configuration.setShowIntroMessage = false
                Shake.configuration.isAutoVideoRecordingEnabled = true
                Shake.configuration.isSensitiveDataRedactionEnabled = false
                Shake.start(clientId: "MV6iYId9D7ONdjKxt9OI0kSoLTYz8co9crNOsRhR",
                            clientSecret: "0TqfIE6nNFtowGTxxee67qq2JJexFkrYysugUtptht6DJHKMTc049hB")
            }
        }
        customizeApp()
    }

    private func customizeApp() {
        UITabBarItem.appearance().setTitleTextAttributes([
            NSAttributedString.Key.font: R.font.robotoRegular.uifont(size: 12)
        ], for: .normal)
        UINavigationBar.appearance().titleTextAttributes = [
            NSAttributedString.Key.font: R.font.robotoMedium.uifont(size: 20),
            NSAttributedString.Key.foregroundColor: R.color.onSurface.uiColor
        ]
        UINavigationBar.appearance().largeTitleTextAttributes = [
            NSAttributedString.Key.font: R.font.robotoMedium.uifont(size: 30),
            NSAttributedString.Key.foregroundColor: R.color.onSurface.uiColor
        ]
        UISearchBar.appearance().setScopeBarButtonTitleTextAttributes([
            NSAttributedString.Key.font: R.font.poppinsSemiBold.uifont(size: 13)
        ], for: .normal)
        UIBarButtonItem.appearance(whenContainedInInstancesOf: [UISearchBar.self])
            .setTitleTextAttributes([
                NSAttributedString.Key.font: R.font.robotoRegular.uifont(size: 16)
            ], for: .normal)
    }

    var body: some Scene {
        WindowGroup {
            ZStack {
                if isLoaded {
                    AppRoot()
                        .environmentObject(theme)
                        .environmentObject(appContext)
                        .environment(\.locale, currentLocal)
                        .environment(\.font, R.font.robotoRegular.font(size: 16))
                        .accentColor(R.color.primary.color)
                        .tint(R.color.primary.color)
                } else {
                    EmptyView()
                }
            }
            .preferredColorScheme(customColorScheme)
            .task {
                await loadAppContent()
            }
            .onChange(of: appContext.screenMode) { value in
                switch value {
                case .light:
                    customColorScheme = .light
                case .dark:
                    customColorScheme = .dark
                default:
                    customColorScheme = nil
                }
            }
        }
    }

    private func loadAppContent() async {
        guard !isLoaded else { return }
        AppLogger.shared.i(messageString: "START INIT APP CONTENT")
        do {
            try await SharedApp.shared.start(context: nil)
            AppLogger.shared.i(messageString: "DEPS LOADED")
            trackAppLifeCycle()
            AppLogger.shared.i(messageString: "TRACKING APP LIFE CYCLE LOADED")
            try await AppContext.shared.load()
            AppLogger.shared.i(messageString: "DB LOADED")
            theme.update(screenMode: AppContext.shared.screenMode)
            AppLogger.shared.i(messageString: "THEMING LOADED")
            initLang()
            AppLogger.shared.i(messageString: "LANG LOADED")
        } catch {
            AppLogger.shared.e(messageString: "App failed to Load",
                               throwable: error)
            fatalError("App is not initialized \(error)")
        }
        isLoaded = true
    }

    private func trackAppLifeCycle() {
        NotificationCenter.default.addObserver(forName: UIApplication.didBecomeActiveNotification,
                                               object: nil,
                                               queue: .main) { _ in
            AppLogger.companion.d(messageString: "didBecomeActiveNotification")
            trackEvent(name: "didBecomeActiveNotification")
        }
        NotificationCenter.default.addObserver(forName: UIApplication.didEnterBackgroundNotification,
                                               object: nil,
                                               queue: .main) { _ in
            AppLogger.companion.d(messageString: "didEnterBackgroundNotification")
            trackEvent(name: "didEnterBackgroundNotification")
        }
        NotificationCenter.default.addObserver(forName: UIApplication.willEnterForegroundNotification,
                                               object: nil,
                                               queue: .main) { _ in
            AppLogger.companion.d(messageString: "willEnterForegroundNotification")
            trackEvent(name: "willEnterForegroundNotification")
        }
        NotificationCenter.default.addObserver(forName: UIApplication.willResignActiveNotification,
                                               object: nil,
                                               queue: .main) { _ in
            AppLogger.companion.d(messageString: "willResignActiveNotification")
            trackEvent(name: "willResignActiveNotification")
        }
        NotificationCenter.default.addObserver(forName: UIApplication.didReceiveMemoryWarningNotification,
                                               object: nil,
                                               queue: .main) { _ in
            AppLogger.companion.d(messageString: "didReceiveMemoryWarningNotification")
            trackEvent(name: "didReceiveMemoryWarningNotification")
        }
    }

    private func initLang() {
        if AppContext.shared.currentLanguageCode == Language.unknown {
            let current = Language.companion.fromCode(code: Locale.current.language.languageCode?.identifier ?? "en")
            AppContext.shared.currentLanguageCode = current == .unknown ? .english : .french
        }
    }
}
