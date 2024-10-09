//
//  CustomSearchScope.swift
//  CACDGAME
//
//  Created by François Dabonot on 18/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import Shared
import SwiftUIIntrospect

enum SearchScope: Int, CaseIterable {
    case all = 0, design = 1, tech = 2, product = 3

    var name: String {
        switch self {
        case .all:
            return R.string.localizable.label_mix.string
        case .design:
            return R.string.localizable.label_design.string
        case .tech:
            return R.string.localizable.label_tech.string
        case .product:
            return R.string.localizable.label_produit.string
        }
    }

    func isSelected(_ current: Int?) -> Bool {
        switch self {
        case .design:
            return current == 0 || current == self.rawValue
        case .tech:
            return current == 0 || current == self.rawValue
        case .product:
            return current == 0 || current == self.rawValue
        default:
            return true
        }
    }
}

extension View {

    @ViewBuilder
    func searchScopes(searchScope: Binding<SearchScope>) -> some View {
        if #available(iOS 16.4, *) {
            self.searchScopes(searchScope, activation: .onSearchPresentation) {
                ForEach(SearchScope.allCases, id: \.self) { scope in
                    Text(scope.name.capitalized)
                        .tag(scope.rawValue)
                }
            }
        } else {
            self.searchScopes(searchScope) {
                ForEach(SearchScope.allCases, id: \.self) { scope in
                    Text(scope.name.capitalized)
                        .tag(scope.rawValue)
                }
            }
        }
    }

    func mpresentationBackground<S>(_ style: S) -> some View where S: ShapeStyle {
        if #available(iOS 16.4, *) {
            return self.presentationBackground(style)
        } else {
            return self.introspect(.sheet, on: .iOS(.v16, .v17)) {
                $0.presentedViewController.view.backgroundColor = .clear
            }
        }
    }

    func popoverPresentationCompactAdaptation() -> some View {
        if #available(iOS 16.4, *) {
            return self.presentationCompactAdaptation(.popover)
        } else {
            return self.presentationDetents([.fraction(0.3)])
        }
    }
}
