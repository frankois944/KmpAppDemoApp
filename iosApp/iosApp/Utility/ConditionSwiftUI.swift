//
//  ConditionSwiftUI.swift
//  CACDGAME
//
//  Created by François Dabonot on 19/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import Foundation
import SwiftUI

extension View {
    @ViewBuilder
    func `if`<Transform: View>(_ condition: Bool,
                               true: ((Self) -> Transform)? = nil,
                               false: ((Self) -> Transform)? = nil) -> some View {
        if condition, let `true` = `true` {
            `true`(self)
        } else if !condition, let `false` = `false` {
            `false`(self)
        } else {
            self
        }
    }
}
