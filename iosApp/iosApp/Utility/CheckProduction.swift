//
//  CheckProduction.swift
//  CACDGAME
//
//  Created by François Dabonot on 23/08/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import Foundation

extension Bundle {
    var isProduction: Bool {
        #if DEBUG
        return false
        #else
        guard let path = self.appStoreReceiptURL?.path else {
            return true
        }
        return !path.contains("sandboxReceipt")
        #endif
    }

    var isPreview: Bool {
        return ProcessInfo.processInfo.environment["XCODE_RUNNING_FOR_PREVIEWS"] == "1"
    }
}
