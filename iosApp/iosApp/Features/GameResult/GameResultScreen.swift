//
//  GameResultScreen.swift
//  CACDGAME
//
//  Created by François Dabonot on 06/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import Shared
import Lottie
import SwiftUIIntrospect

struct GameResultScreen: View {

    static let route = GameResultModel.self

    @EnvironmentObject var appContext: AppContext
    @EnvironmentObject var router: Router
    @State var historyData: HistoryData!
    @State var displayResultBanner: Bool = true
    @State var showResultLoader: Bool = true

    let historyDataId: Int
    let fromGameResult: Bool

    init(model: GameResultModel) {
        self.historyDataId = model.historyId
        self.historyData = model.historyData
        self.fromGameResult = model.fromGameResult
    }

    var body: some View {
        VStack {
            ScrollViewReader { reader in
                if showResultLoader && fromGameResult {
                    GameResultLoaderView(initialSeconds: 3, isDisplayed: $showResultLoader)
                        .task {
                            do {
                                if historyData == nil {
                                    historyData = try await Database.companion.data.getHistory(historyId: Int64(historyDataId))
                                    try await indexingDataForSpotLight(language: appContext.currentLanguageCode) {
                                        $0 == 1
                                    }
                                }
                            } catch {
                                AppLogger.shared.e(messageString: "Can load history", throwable: error)
                                router.popToRoot()
                            }
                        }
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
                } else {
                    if let historyData = historyData {
                        ScrollView {
                            GameResultView(historyData: historyData) {
                                reader.scrollTo("GameResultPart2View")
                            }
                            GameResultPart2View().id("GameResultPart2View")
                        }
                        .modifier(CommonCardBackground())
                    }
                }
            }
            .edgesIgnoringSafeArea(.bottom)
            .overlay {
                if fromGameResult && !showResultLoader && displayResultBanner {
                    ResultAnimationView(isPlaying: $displayResultBanner)
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
                }
            }
            .toolbar {
                if fromGameResult {
                    ToolbarItem(placement: .navigationBarTrailing, content: {
                        Button {
                            router.popToRoot()
                        } label: {
                            R.image.close.image
                                .foregroundColor(R.color.primary.color)
                        }
                    })
                }
            }
        }
        .if(showResultLoader && fromGameResult, true: { view in
            view.toolbar(.hidden, for: .navigationBar)
        }, false: { view in
            view.toolbar(.visible, for: .navigationBar)
        })
    }
}

#Preview {
    GameResultScreen(model: .init(historyData: HistoryData.companion.dummy,
                                  fromGameResult: false,
                                  showResultLoader: false,
                                  shouldDisplayResultBanner: false))
        .environmentObject(AppContext())
        .environmentObject(Router())
}
