//
//  btnBack.swift
//  CACDGAME
//
//  Created by François Dabonot on 17/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI

struct BtnBack: View {

    @Environment(\.presentationMode) var presentationMode: Binding

    var body: some View {
        Button {
            self.presentationMode.wrappedValue.dismiss()
        } label: {
            R.image.backButton.image
                .aspectRatio(contentMode: .fit)
                .foregroundColor(R.color.primary.color)
            Text("  ")
        }
        .navigationBarBackButtonHidden(true)
    }
}

#Preview {
    BtnBack()
}
