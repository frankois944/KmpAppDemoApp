//
//  GameResultPart2View.swift
//  CACDGAME
//
//  Created by François Dabonot on 24/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI

struct GameResultPart2View: View {

    @Environment(\.openURL) var openURL
    @EnvironmentObject var router: Router

    var body: some View {
        HStack(alignment: .top) {
            R.image.info.image
            CustomAttributedText(stringRes: R.string.localizable.description_full_company.key,
                                 custom: [
                                    .ITALIC: .init(font: R.font.poppinsSemiBold.font(size: 15)),
                                    .BOLD: .init(font: R.font.poppinsSemiBold.font(size: 15)),
                                    .DEFAULT: .init(font: R.font.poppinsRegular.font(size: 15))
                                 ])
        }
        .padding()
        .background(R.color.primary.color.opacity(0.12))
        PrimaryButton(R.string.localizable.go_to_website.string,
                      leftImageName: R.image.goOutside.name) {
            trackEvent(name: "OPEN WEBSITE")
            openURL(URL(string: "https://www.cacd2.fr")!)
        }
        .padding()
        PrimaryButton(R.string.localizable.go_to_contact.string) {
            router.push(route: .contact(model: .init()))
        }
        .padding()
    }
}

#Preview {
    GameResultPart2View()
        .environmentObject(AppContext())
        .environmentObject(Router())
        .previewLayout(.sizeThatFits)
}
