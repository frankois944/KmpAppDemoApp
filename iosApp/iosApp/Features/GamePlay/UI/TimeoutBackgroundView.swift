//
//  TimeoutBackgroundView.swift
//  CACDGAME
//
//  Created by François Dabonot on 19/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import Lottie

struct TimeoutBackgroundView: View {

    let progress: Double

    var body: some View {
        GeometryReader { geometry in
            VStack(spacing: 0) {
                Spacer()
                LottieView(animation: LottieAnimation.named(R.file.waveJson.name))
                    .looping()
                    .resizable()
                    .configure { configure in
                        configure.contentMode = .scaleToFill
                    }
                    .frame(height: 30)
                R.color.primary.color.frame(height: geometry.size.height * progress)
            }
            .ignoresSafeArea(.all)
            .opacity(0.20)
        }
    }
}

#Preview {
    TimeoutBackgroundView(progress: 0.5)
        .environmentObject(AppContext())
}
