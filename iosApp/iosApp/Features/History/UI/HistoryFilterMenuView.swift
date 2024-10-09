//
//  HistoryFilterMenuView.swift
//  CACDGAME
//
//  Created by François Dabonot on 07/08/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import Shared

extension HistoryFilterCriteria: ObservableObject {
}

struct HistoryFilterMenuView: View {

    @Binding var filterCriteria: HistoryFilterCriteria

    @State private var design: Bool
    @State private var tech: Bool
    @State private var product: Bool
    @State private var mix: Bool
    @State private var availableCategory: [GameChoice] = []

    init(filterCriteria: Binding<HistoryFilterCriteria>) {
        self._filterCriteria = filterCriteria
        self.design = filterCriteria.designSelected.wrappedValue
        self.tech = filterCriteria.techSelected.wrappedValue
        self.product = filterCriteria.productSelected.wrappedValue
        self.mix = filterCriteria.mixSelected.wrappedValue
    }

    private func updateCriteria() {
        filterCriteria = HistoryFilterCriteria(designSelected: design,
                                               techSelected: tech,
                                               productSelected: product,
                                               mixSelected: mix)
    }

    var body: some View {
        VStack(spacing: 12) {
            if availableCategory.contains(.design) {
                Toggle(R.string.localizable.selection_quiz_design.string, isOn: $design)
                    .font(R.font.robotoRegular.font(size: 15))
            }
            if availableCategory.contains(.tech) {
                Toggle(R.string.localizable.selection_quiz_tech.string, isOn: $tech)
                    .font(R.font.robotoRegular.font(size: 15))
            }
            if availableCategory.contains(.product) {
                Toggle(R.string.localizable.selection_quiz_produit.string, isOn: $product)
                    .font(R.font.robotoRegular.font(size: 15))
            }
            if availableCategory.contains(.all) {
                Toggle(R.string.localizable.selection_mix.string, isOn: $mix)
                    .font(R.font.robotoRegular.font(size: 15))
            }
        }
        .padding()
        .onChange(of: design) { _ in
            updateCriteria()
        }.onChange(of: tech) { _ in
            updateCriteria()
        }.onChange(of: product) { _ in
            updateCriteria()
        }.onChange(of: mix) { _ in
            updateCriteria()
        }
        .task {
            do {
                availableCategory = try await GameChoice.companion.getAvailableHistoryChoice()
            } catch {
                AppLogger.shared.e(messageString: "Can't retrieve available category")
            }
        }
    }
}

#Preview {
    HistoryFilterMenuView(filterCriteria: .constant(.init(designSelected: true,
                                                          techSelected: true,
                                                          productSelected: true,
                                                          mixSelected: true)))
}
