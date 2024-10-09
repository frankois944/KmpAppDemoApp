//
//  GameResultChartView.swift
//  CACDGAME
//
//  Created by François Dabonot on 24/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import DGCharts

struct GameResultChartView: UIViewRepresentable {

    let okData: Double
    let koData: Double
    let fontSizeCorrectQuestion: CGFloat
    let fontSizeTotalQuestion: CGFloat
    let withIcons: Bool
    let size: CGSize
    let okColor = R.color.primary.uiColor
    let koColor = R.color.error.uiColor

    init(okData: Double,
         koData: Double,
         fontSizeCorrectQuestion: CGFloat,
         fontSizeTotalQuestion: CGFloat,
         withIcons: Bool,
         size: CGSize) {
        self.okData = okData
        self.koData = koData
        self.fontSizeCorrectQuestion = fontSizeCorrectQuestion
        self.fontSizeTotalQuestion = fontSizeTotalQuestion
        self.withIcons = withIcons
        self.size = size
    }

    func makeUIView(context: Context) -> PieChartView {
        return PieChartView(frame: .init(origin: .zero, size: size))
    }

    private func generateCenterSpannableText() -> NSAttributedString {
        let result = NSMutableAttributedString()
        let part1 = NSAttributedString(string: "\(Int(okData))", attributes: [
            NSAttributedString.Key.font: R.font.poppinsExtraBold.uifont(size: fontSizeCorrectQuestion),
            NSAttributedString.Key.foregroundColor: okColor
        ])
        result.append(part1)
        let part2 = NSAttributedString(string: "/\(Int(okData + koData))", attributes: [
            NSAttributedString.Key.font: R.font.poppinsLight.uifont(size: fontSizeTotalQuestion),
            NSAttributedString.Key.foregroundColor: okColor
        ])
        result.append(part2)
        return result
    }

    func updateUIView(_ chart: PieChartView, context: Context) {
        var entries = [PieChartDataEntry]()
        entries.append(
            withIcons ?
                PieChartDataEntry(value: koData, icon: R.image.star_empty.uiImage)
                : PieChartDataEntry(value: koData)
        )
        entries.append(
            withIcons ?
                PieChartDataEntry(value: okData, icon: R.image.start_full.uiImage)
                : PieChartDataEntry(value: okData)
        )

        let dataSet = PieChartDataSet(entries: entries)
        dataSet.drawIconsEnabled = true
        dataSet.sliceSpace = 0
        dataSet.selectionShift = 0
        dataSet.drawValuesEnabled = false

        var colors = [UIColor]()
        colors.append(koColor)
        colors.append(okColor)
        dataSet.colors = colors

        chart.data = PieChartData(dataSet: dataSet)
        chart.highlightValue(nil)
        chart.drawEntryLabelsEnabled = false

        chart.holeRadiusPercent =  0.65
        chart.transparentCircleRadiusPercent = 0.7
        chart.transparentCircleColor = R.color.chartInnerColor.uiColor
        chart.chartDescription.enabled = false
        chart.legend.enabled = false
        chart.holeColor = .clear

        chart.centerAttributedText = generateCenterSpannableText()
        chart.drawCenterTextEnabled = true
        chart.frame = CGRect(origin: .zero, size: size)
        chart.isUserInteractionEnabled = false
    }

}

#Preview {
    GameResultChartView(okData: 6,
                        koData: 5,
                        fontSizeCorrectQuestion: 54,
                        fontSizeTotalQuestion: 20,
                        withIcons: true,
                        size: .init(width: 185, height: 185))
        .previewLayout(.fixed(width: 185, height: 185))
        .environmentObject(AppContext())
}
