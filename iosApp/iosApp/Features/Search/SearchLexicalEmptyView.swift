//
//  SearchLexicalEmptyView.swift
//  CACDGAME
//
//  Created by François Dabonot on 06/12/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI

struct SearchLexicalEmptyView: View {
    var body: some View {
        VStack(alignment: .center, content: {
            R.image.lexicon_empty.image
            Text(R.string.localizable.lexical_empty_list.localizedKey)
                .multilineTextAlignment(.center)
                .padding()
                .font(R.font.poppinsSemiBold.font(size: 18))
        })
    }
}

#Preview {
    SearchLexicalEmptyView()
}
