//
//  GameResultScreenModel.swift
//  CACDGAME
//
//  Created by François Dabonot on 25/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import Foundation
import Shared

struct GameResultModel: Hashable {

    let historyData: HistoryData?
    let historyId: Int
    let showResultLoader: Bool
    let shouldDisplayResultBanner: Bool
    let fromGameResult: Bool

    init(historyData: HistoryData,
         fromGameResult: Bool,
         showResultLoader: Bool = false,
         shouldDisplayResultBanner: Bool = false
    ) {
        self.historyData = historyData
        self.historyId = Int(historyData.id)
        self.fromGameResult = fromGameResult
        self.showResultLoader = showResultLoader
        self.shouldDisplayResultBanner = shouldDisplayResultBanner
    }

    init(historyId: Int,
         fromGameResult: Bool,
         showResultLoader: Bool = false,
         shouldDisplayResultBanner: Bool = false) {
        self.historyId = historyId
        self.historyData = nil
        self.fromGameResult = fromGameResult
        self.showResultLoader = showResultLoader
        self.shouldDisplayResultBanner = shouldDisplayResultBanner
    }
}
