//
//  QuestionChoice.swift
//  CACDGAME
//
//  Created by François Dabonot on 03/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import Foundation
import Shared
import SwiftUI

extension QuestionChoice {

    func image() -> Image {
        switch self.game {
        case .design:
            return  R.image.rectangle_1.image
        case .product:
            return  R.image.rectangle_2.image
        default:
            return R.image.rectangle_3.image
        }
    }
}

extension QuestionChoice: Identifiable {

}

extension Array where Element == QuestionChoice {
    func nbOkResponse() -> Double {
        Double(self.filter {
            $0.userSelectedAnswer?.isCorrect == true
        }.count)
    }

    func nbKoResponse() -> Double {
        Double(self.filter {
            $0.userSelectedAnswer?.isCorrect == false
        }.count)
    }

    func averageMsResponseTime() -> Double {
        let sumOfTime = Double(self.reduce(0) { $0 + $1.responseTime })
        let totalTime = (sumOfTime / Double(self.count)).rounded()
        return totalTime < 1 ? 1 : totalTime
    }
}
