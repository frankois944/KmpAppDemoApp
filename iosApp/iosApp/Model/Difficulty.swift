//
//  Difficulty.swift
//  CACDGAME
//
//  Created by François Dabonot on 03/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import Foundation
import Shared
import SwiftUI

extension Difficulty {

    func label() -> String {
        switch self {
        case .easy:
            return R.string.localizable.game_niveau_1_debutant.string
        case .medium:
            return R.string.localizable.game_niveau_2_confirme.string
        default:
            return R.string.localizable.game_niveau_3_incollable.string
        }
    }

    func number() -> Int {
        switch self {
        case .easy:
            return 1
        case .medium:
            return 2
        default:
            return 3
        }
    }

    func shortname() -> String {
        switch self {
        case .easy:
            return R.string.localizable.level_novice_name.string
        case .medium:
            return R.string.localizable.level_confirm_name.string
        default:
            return R.string.localizable.level_expert_name.string
        }
    }

    func image() -> String {
        switch self {
        case .easy:
            return R.image.niveau1.name
        case .medium:
            return R.image.niveau2.name
        default:
            return R.image.niveau3.name
        }
    }

    func selectedimage() -> String {
        switch self {
        case .easy:
            return R.image.lvl1_selected.name
        case .medium:
            return R.image.lvl2_selected.name
        default:
            return R.image.lvl3_selected.name
        }
    }

    func title() -> String {
        switch self {
        case .easy:
            return R.string.localizable.level_1.string
        case .medium:
            return R.string.localizable.level_2.string
        default:
            return R.string.localizable.level_3.string
        }
    }

    func content() -> String {
        switch self {
        case .easy:
            return R.string.localizable.level_novice.string
        case .medium:
            return R.string.localizable.level_confirm.string
        default:
            return R.string.localizable.level_expert.string
        }
    }

}
