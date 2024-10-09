//
//  ResultAnimationView.swift
//  CACDGAME
//
//  Created by François Dabonot on 12/09/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import Lottie

struct ResultAnimationView: View {

    @Binding var isPlaying: Bool

    var body: some View {
        if isPlaying {
            LottieView(animation: LottieAnimation.named(R.file.result_animationJson.name)!)
                .resizable()
                .configure { configure in
                    configure.contentMode = .scaleToFill
                    configure.backgroundBehavior = .forceFinish
                    configure.play { _ in
                        isPlaying = false
                    }
                }
                .if(isPlaying, true: { view in
                    view.playing().opacity(1)
                })
                .onTapGesture {
                    isPlaying = false
                }
        }
    }
}

#Preview {
    ResultAnimationView(isPlaying: .constant(true))
}
