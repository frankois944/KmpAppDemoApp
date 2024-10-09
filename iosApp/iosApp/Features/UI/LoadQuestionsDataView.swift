//
//  LoadQuestionsDataView.swift
//  CACDGAME
//
//  Created by François Dabonot on 06/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import Shared
import Lottie
import CoreSpotlight

struct LoadQuestionsDataView: View {

    @Environment(\.colorScheme) var colorSchame
    var language: Language?
    let onCompletion: () -> Void
    var animationName: String {
        if colorSchame == .light {
            return R.file.loaderJson.name
        } else {
            return R.file.loaderDarkJson.name
        }
    }
    @State var uiState: UIState<Void> = .loading

    var body: some View {
        VStack {
            switch uiState {
            case .error(let error):
                Text(error.localizedDescription)
            default:
                LottieView(animation: LottieAnimation.named(animationName)!)
                    .resizable()
                    .looping()
            }
        }.onAppear {
            Task {
                do {
                    try await DatoCMSAPI.shared.fetchAllData(language: language)
                    try await indexingDataForSpotLight(language: language ?? .french)
                    onCompletion()
                } catch {
                    uiState = .error(error)
                }
            }
        }
    }
}

#Preview {
    LoadQuestionsDataView(language: .english) {
    }.environmentObject(AppContext())
}
