//
//  CustomKotlinThrowable.swift
//  CACDGAME
//
//  Created by François Dabonot on 03/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import Foundation
import Shared

/// Functions for logging message with Error type
extension KermitLogger {

    func a(messageString: String, throwable: Error) {
        self.a(messageString: messageString, throwable: throwable.asKotlinThrowable)
    }

    func d(messageString: String, throwable: Error) {
        self.d(messageString: messageString, throwable: throwable.asKotlinThrowable)
    }

    func e(messageString: String, throwable: Error) {
        self.e(messageString: messageString, throwable: throwable.asKotlinThrowable)
    }

    func i(messageString: String, throwable: Error) {
        self.i(messageString: messageString, throwable: throwable.asKotlinThrowable)
    }

    func v(messageString: String, throwable: Error) {
        self.v(messageString: messageString, throwable: throwable.asKotlinThrowable)
    }

    func w(messageString: String, throwable: Error) {
        self.w(messageString: messageString, throwable: throwable.asKotlinThrowable)
    }
}

extension Error {

    var asKotlinThrowable: KotlinThrowable {
        return KotlinThrowable(message: self.localizedDescription,
                               cause: KotlinThrowable(message: "\(self)"))
    }
}
