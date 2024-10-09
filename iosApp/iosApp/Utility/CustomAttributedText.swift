//
//  CustomAttributedText.swift
//  CACDGAME
//
//  Created by François Dabonot on 27/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import Shared
import RswiftResources

enum AttributedStyle {
    case ITALIC, BOLD, DEFAULT
}

struct AttributedFontStyle {
    let font: Font
    let color: Color?

    init(font: Font, color: Color? = nil) {
        self.font = font
        self.color = color
    }
}

extension Data {
    var html2AttributedString: NSAttributedString? {
        do {
            return try NSAttributedString(data: self,
                                          options: [
                                            .documentType: NSAttributedString.DocumentType.html,
                                            .characterEncoding: String.Encoding.utf8.rawValue
                                          ],
                                          documentAttributes: nil)
        } catch {
            print("error:", error)
            return  nil
        }
    }
    var html2String: String { html2AttributedString?.string ?? "" }
}

extension String {

    var html2AttributedString: NSAttributedString? {
        Data(utf8).html2AttributedString
    }

    var html2String: String {
        html2AttributedString?.string ?? ""
    }
}

struct CustomAttributedText: View {

    @EnvironmentObject var appContext: AppContext

    @State private var internalAttributedText: AttributedString?
    @State private var latestColorColor: ScreenMode?
    private let stringRes: StaticString
    private let custom: [AttributedStyle: AttributedFontStyle]
    private let args: [CVarArg]
    @State private var currentContent: AttributedString!

    private func getString(key: StaticString, _ args: [CVarArg]) -> String {
        let currentLocalId = currentLocal.language.languageCode?.identifier ?? "en"
        guard let bundlePath = Bundle.main.path(forResource: currentLocalId, ofType: "lproj"),
              let bundle = Bundle(path: bundlePath) else {
            let defaultVal = NSLocalizedString(key.description, comment: "")
            return String(format: defaultVal, locale: currentLocal, arguments: args)
        }
        let trad = NSLocalizedString(key.description, tableName: nil, bundle: bundle, comment: "")
        return String(format: trad, locale: currentLocal, arguments: args)
    }

    private func buildText() -> AttributedString {

        var defaultFont = R.font.poppinsMedium.font(size: 20)
        var defaultColor = R.color.onSurface.color
        if let `default` = custom[.DEFAULT] {
            defaultFont = `default`.font
            if let color = `default`.color {
                defaultColor = color
            }
        }

        var boldFont = R.font.poppinsBold.font(size: 20)
        var boldColor = R.color.onSurface.color
        if let bold = custom[.BOLD] {
            boldFont = bold.font
            if let color = bold.color {
                boldColor = color
            }
        }

        var italicFont = R.font.poppinsBold.font(size: 20)
        var italicColor = R.color.primary.color
        if let italic = custom[.ITALIC] {
            italicFont = italic.font
            if let color = italic.color {
                italicColor = color
            }
        }

        let source = getString(key: stringRes, args)
        let attributedString = NSMutableAttributedString(attributedString: source.html2AttributedString!)

        let range = NSRange(location: 0, length: attributedString.length)
        let style = NSMutableParagraphStyle()
        style.paragraphSpacing = 8

        var result = AttributedString("")

        attributedString.enumerateAttribute(.font, in: range) { value, range, _ in
            guard let currentFont = value as? UIFont else { return }

            let isBold = currentFont.fontName.lowercased().contains("bold")
            let isItalic = currentFont.fontName.lowercased().contains("italic")
            let content = attributedString.attributedSubstring(from: range).string

            if isBold {
                var newLine = AttributedString(content)
                newLine.font = boldFont
                newLine.foregroundColor = boldColor
                // swiftlint:disable:next shorthand_operator
                result = result + newLine
            } else if isItalic {
                var newLine = AttributedString(content)
                newLine.font = italicFont
                newLine.foregroundColor = italicColor
                // swiftlint:disable:next shorthand_operator
                result = result + newLine
            } else {
                var newLine = AttributedString(content)
                newLine.font = defaultFont
                newLine.foregroundColor = defaultColor
                // swiftlint:disable:next shorthand_operator
                result = result + newLine
            }
        }
        return result
    }

    init(stringRes: StaticString,
         custom: [AttributedStyle: AttributedFontStyle] = [:],
         _ args: CVarArg...) {
        self.stringRes = stringRes
        self.custom = custom
        self.args = args
    }

    var body: some View {
        Text(currentContent ?? "").onAppear {
            currentContent = currentContent ?? buildText()
        }
    }
}
#Preview {
    VStack {
        Text(R.string.localizable.done_title.localizedKey)
        CustomAttributedText(stringRes: R.string.localizable.quiz_selection_content.key, "fgh")
    }
    .environmentObject(AppContext())
}
