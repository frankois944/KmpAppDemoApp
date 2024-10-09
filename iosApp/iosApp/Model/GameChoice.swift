//
//  GameChoice.swift
//  CACDGAME
//
//  Created by François Dabonot on 03/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import Foundation
import Shared
import SwiftUI

extension GameChoice {

    func image(selectedGameId: GameChoice?) -> String {
        let isSelected = selectedGameId?.gameId == gameId
        switch self {
        case .design:
            if isSelected {
                return R.image.design_selected.name
            } else {
                return R.image.design_normal.name
            }
        case .product:
            if isSelected {
                return R.image.product_selected.name
            } else {
                return R.image.product_img.name
            }
        case .tech:
            if isSelected {
                return R.image.tech_selected.name
            } else {
                return R.image.tech_normal.name
            }
        default:
            if isSelected {
                return R.image.mix_selected.name
            } else {
                return R.image.mix_normal.name
            }
        }
    }

    func title() -> LocalizedStringKey {
        switch self {
        case .design:
            return R.string.localizable.selection_quiz_design.localizedKey
        case .product:
            return R.string.localizable.selection_quiz_produit.localizedKey
        case .tech:
            return R.string.localizable.selection_quiz_tech.localizedKey
        default:
            return R.string.localizable.selection_mix.localizedKey
        }
    }

    func label() -> LocalizedStringKey {
        switch self {
        case .design:
            return R.string.localizable.label_design.localizedKey
        case .product:
            return R.string.localizable.label_produit.localizedKey
        case .tech:
            return R.string.localizable.label_tech.localizedKey
        default:
            return R.string.localizable.label_mix.localizedKey
        }
    }

    func color(selectedGameId: GameChoice?) -> Color {
        let isSelected = selectedGameId?.gameId == self.gameId
        if isSelected {
            return R.color.primary.color
        } else {
            return  R.color.onSurface.color
        }
    }

}
