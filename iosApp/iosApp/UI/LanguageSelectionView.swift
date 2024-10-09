//
//  LanguageSelectionView.swift
//  CACDGAME
//
//  Created by François Dabonot on 10/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import Shared

struct LanguageSelectionView: View {

    @EnvironmentObject var appContext: AppContext
    @State private var selection: Language = .french

    let leftIcnSpace: CGFloat?

    init(leftIcnSpace: CGFloat? = nil) {
        self.leftIcnSpace = leftIcnSpace
    }

    var body: some View {
        HStack {
            R.image.language.image
                .foregroundColor(R.color.primary.color)
                .if(leftIcnSpace != nil, true: { view in
                    view.padding(.trailing, leftIcnSpace!)
                })
            Text(R.string.localizable.current_selected_lang.localizedKeyWithFormat(""))
                .foregroundColor(R.color.onSurface.color)
            if leftIcnSpace == nil {
                Spacer()
            }
            Picker("", selection: $selection) {
                Text(R.string.localizable.french_lang_selector.localizedKey).tag(Language.french)
                Text(R.string.localizable.english_lang_selector.localizedKey).tag(Language.english)
            }
            .foregroundColor(R.color.primary.color)
        }
        .font(R.font.robotoRegular.font(size: 16))
        .pickerStyle(.menu)
        .onAppear {
            selection = appContext.currentLanguageCode
        }
        .onChange(of: appContext.currentLanguageCode,
                  perform: {
                    if $0 != selection {
                        selection = appContext.currentLanguageCode
                    }
                  })
        .onChange(of: selection) { newValue in
            if newValue != appContext.currentLanguageCode {
                switch newValue {
                case .french:
                    trackEvent(name: "Change language for French")
                case .english:
                    trackEvent(name: "Change language for English")
                default:
                    break
                }
                appContext.currentLanguageCode = newValue
            }
        }
    }
}

#Preview {
    LanguageSelectionView()
        .environmentObject(AppContext())
}
