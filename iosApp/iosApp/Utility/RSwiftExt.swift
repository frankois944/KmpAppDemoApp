//
//  RSwiftExt.swift
//  CACDGAME
//
//  Created by François Dabonot on 05/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import Foundation
import SwiftUI
import RswiftResources
import Shared

// MARK: - ImageResource
extension RswiftResources.ImageResource {

    var image: Image {
        Image(name)
    }

    var uiImage: UIImage {
        UIImage(named: self.name)!
    }
}

// MARK: - ColorResource
extension RswiftResources.ColorResource {

    var color: Color {
        Color(name)
    }

    var uiColor: UIColor {
        UIColor(named: name)!
    }
}

extension Color {

    var uiColor: UIColor {
        UIColor(self)
    }
}

// MARK: - Localisation
private func internalLocalizedStringKeyWithFormat(key: StaticString, _ args: CVarArg...) -> String {
    let currentLocalId = currentLocal.language.languageCode?.identifier ?? "en"
    guard let bundlePath = Bundle.main.path(forResource: currentLocalId, ofType: "lproj"),
          let bundle = Bundle(path: bundlePath) else {
        let defaultVal = NSLocalizedString(key.description, comment: "")
        return String(format: defaultVal, locale: currentLocal, arguments: args)
    }
    let trad = NSLocalizedString(key.description, tableName: nil, bundle: bundle, comment: "")
    return String(format: trad, locale: currentLocal, arguments: args)
}

extension RswiftResources.StringResource3<String, String, String> {
    func localizedKeyWithFormat(_ args: CVarArg...) -> String {
        internalLocalizedStringKeyWithFormat(key: key, args)
    }
}

extension RswiftResources.StringResource2<String, String> {
    func localizedKeyWithFormat(_ args: CVarArg...) -> String {
        internalLocalizedStringKeyWithFormat(key: key, args)
    }
}

extension RswiftResources.StringResource1<String> {
    func localizedKeyWithFormat(_ args: CVarArg...) -> String {
        internalLocalizedStringKeyWithFormat(key: key, args)
    }
}

extension StringResource {

    var string: String {
        internalLocalizedStringKeyWithFormat(key: key, [CVarArg]())
    }

    var localizedKey: LocalizedStringKey {
        LocalizedStringKey(key.description)
    }
}

// MARK: - FontResource
extension FontResource {

    func font(size: CGFloat, relativeTo textStyle: Font.TextStyle? = nil) -> SwiftUI.Font {
        if let textStyle = textStyle {
            return SwiftUI.Font.custom(self.name, size: size, relativeTo: textStyle)
        } else {
            return SwiftUI.Font.custom(self.name, size: size)
        }
    }

    func uifont(size: CGFloat) -> UIFont {
        UIFont(name: self.name, size: size)!
    }
}

extension LocalizedStringKey {

    // This will mirror the `LocalizedStringKey` so it can access its
    // internal `key` property. Mirroring is rather expensive, but it
    // should be fine performance-wise, unless you are
    // using it too much or doing something out of the norm.
    var stringKey: String? {
        Mirror(reflecting: self).children.first(where: { $0.label == "key" })?.value as? String
    }
}
