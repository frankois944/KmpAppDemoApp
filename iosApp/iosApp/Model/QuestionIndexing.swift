//
//  QuestionIndexing.swift
//  CACDGAME
//
//  Created by François Dabonot on 03/01/2024.
//  Copyright © 2024 CACD2. All rights reserved.
//

import Foundation
import CoreSpotlight
import Shared

func indexingDataForSpotLight(language: Language, onlyIf: ((Int) -> Bool)? = nil) async throws {
    try await Task { @MainActor in
        let historyCount = try await Database.companion.data.getHistoryCount().intValue
        var mustSync = historyCount > 0
        if let onlyIf {
            mustSync = onlyIf(historyCount)
        }
        if mustSync {
            AppLogger.shared.d(messageString: "START indexingDataForSpotLight")
            let data = try await Database.companion.data.getAllQuestions(withProposition: false, lang: language)
            var searchableItems = [CSSearchableItem]()
            data.forEach {
                let attributeSet = CSSearchableItemAttributeSet(contentType: .content)
                attributeSet.displayName = $0.content
                let searchableItem = CSSearchableItem(uniqueIdentifier: "\($0.id)",
                                                      domainIdentifier: "questions",
                                                      attributeSet: attributeSet)
                searchableItems.append(searchableItem)
            }
            AppLogger.shared.d(messageString: "indexingDataForSpotLight \(searchableItems.count)")
            try await CSSearchableIndex.default().indexSearchableItems(searchableItems)
            AppLogger.shared.d(messageString: "END indexingDataForSpotLight")
        }
    }.value
}
