//
//  EventTracker.swift
//  CACDGAME
//
//  Created by François Dabonot on 10/08/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import Foundation
import FirebaseCore
import FirebaseAnalytics
import SwiftUI
import Shared

@available(iOS 13.0, macOS 10.15, macCatalyst 13.0, tvOS 13.0, *)
@available(watchOS, unavailable)
public extension View {
    /// Logs `screen_view` events in Google Analytics for Firebase when this view appears on screen.
    /// - Parameters:
    ///   - name: Current screen name logged with the `screen_view` event.
    ///   - class: Current screen class or struct logged with the `screen_view` event.
    ///   - extraParameters: Any additional parameters to be logged. These extra parameters must
    ///       follow the same rules as described in the `Analytics.logEvent(_:parameters:)` docs.
    /// - Returns: A view with a custom `ViewModifier` used to log `screen_view` events when this
    ///    view appears on screen.
    func analyticsScreen(name: String,
                         class: String = "View",
                         extraParameters: [String: String] = [:]) -> some View {
        // `self` is the view, we're just adding an `LoggedAnalyticsModifier` modifier on it.
        modifier(LoggedAnalyticsModifier(screenName: name,
                                         screenClass: `class`,
                                         extraParameters: extraParameters))
    }
}

@available(iOS 13.0, macOS 10.15, macCatalyst 13.0, tvOS 13.0, *)
@available(watchOS, unavailable)
internal struct LoggedAnalyticsModifier: ViewModifier {
    /// The name of the view to log in the `AnalyticsParameterScreenName` parameter.
    let screenName: String

    /// The name of the view to log in the `AnalyticsParameterScreenClass` parameter.
    let screenClass: String

    /// Extra parameters to log with the screen view event.
    let extraParameters: [String: Any]

    func body(content: Content) -> some View {
        // Take the content and add an onAppear action to know when the view has appeared on screen.
        content.onAppear {
            guard !Bundle.main.isPreview else { return }
            // Log the event appearing, adding the appropriate keys and values needed for screen
            // view events.
            AppLogger.shared.d(messageString: "[ENTER]\(screenName)")
            var params = [TrackerDimension: String]()
            extraParameters.forEach { row in
                let dimension = TrackerDimension.companion.fromStringKey(key: row.key)
                if let dimension = dimension {
                    params[dimension] = "\(row.value)"
                }
            }
            Shared.Tracker.shared.instance.trackView(path: KotlinArray<NSString>.init(size: 1, init: { _ in
                NSString(string: screenClass)
            }),
            title: screenName,
            dimensions: params)
        }
        .onDisappear {
            guard !Bundle.main.isPreview else { return  }
            AppLogger.shared.d(messageString: "[EXIT]\(screenName)")
        }
    }
}

func trackEvent(name: String, extra: [String: String]? = nil) {
    var params = [TrackerDimension: String]()
    extra?.forEach { row in
        let dimension = TrackerDimension.companion.fromStringKey(key: row.key)
        if let dimension = dimension {
            params[dimension] = row.value
        }
    }
    Shared.Tracker.shared.instance.trackEvent(category: AnalyticsEventSelectContent,
                                              action: AnalyticsParameterContent,
                                              name: name,
                                              dimensions: params)
}
