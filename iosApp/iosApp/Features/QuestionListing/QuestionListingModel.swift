//
//  QuestionListingModel.swift
//  CACDGAME
//
//  Created by François Dabonot on 25/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

struct QuestionListingModel: Hashable {

    let historyId: Int
    let isSuccess: Bool?

    init(historyId: Int, isSuccess: Bool? = nil) {
        self.historyId = historyId
        self.isSuccess = isSuccess
    }

    var title: String {

        switch isSuccess {
        case true:
            return R.string.localizable.title_good_answsers.string
        case false:
            return R.string.localizable.title_bad_answsers.string
        default:
            return R.string.localizable.title_my_answsers.string
        }
    }
}
