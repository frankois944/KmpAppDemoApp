//
//  GameResultView.swift
//  CACDGAME
//
//  Created by François Dabonot on 24/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import Shared

struct GameResultView: View {

    let historyData: HistoryData
    let goToCompanyDetail: () -> Void

    var body: some View {
        HStack {
            GameResultChartView(okData: historyData.data.nbOkResponse(),
                                koData: historyData.data.nbKoResponse(),
                                fontSizeCorrectQuestion: 54,
                                fontSizeTotalQuestion: 30,
                                withIcons: true,
                                size: .init(width: 185, height: 185))
                .frame(width: 185, height: 185)
            VStack {
                HStack(spacing: 5) {
                    Circle()
                        .frame(width: 10, height: 10, alignment: .center)
                        .foregroundColor(R.color.primary.color)
                    Text(R.string.localizable.result_correct_answser_color.localizedKey)
                        .font(R.font.poppinsRegular.font(size: 12))
                        .foregroundColor(R.color.onSurface.color)
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                HStack(spacing: 5) {
                    Circle()
                        .frame(width: 10, height: 10, alignment: .center)
                        .foregroundColor(R.color.error.color)
                    Text(R.string.localizable.result_bad_answser_color.localizedKey)
                        .font(R.font.poppinsRegular.font(size: 12))
                        .foregroundColor(R.color.onSurface.color)
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                Divider()
                    .padding(.leading)
                    .padding(.trailing)
                HStack(spacing: 0) {
                    R.image.result_category.image
                        .foregroundColor(R.color.primary.color)
                    // problème de trad
                    Text(R.string.localizable.result_level(
                        historyData.data.first?.difficulty.number() ?? 1,
                        historyData.data.first?.difficulty.shortname() ?? "N/A"
                    ))
                    .font(R.font.poppinsRegular.font(size: 12))
                    .foregroundColor(R.color.onSurface.color)
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                HStack(spacing: 0) {
                    R.image.result_timer.image
                        .foregroundColor(R.color.primary.color)
                    Text(R.string.localizable.result_response_time(
                        Int(historyData.data.averageMsResponseTime())
                    ))
                    .font(R.font.poppinsRegular.font(size: 12))
                    .foregroundColor(R.color.onSurface.color)
                    .fixedSize(horizontal: false, vertical: true)
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                Spacer()
            }
            .padding(.top, 10)
        }
        .padding()
        VStack(spacing: 0) {
            if historyData.data.nbOkResponse() > 0 {
                NavigationLink(value: QuestionListingScreen.route.init(historyId: Int(historyData.id),
                                                                       isSuccess: true)) {
                    HStack {
                        R.image.check_good.image.foregroundColor(R.color.primary.color)
                            .padding([.leading])
                        Text(R.string.localizable.result_correct_response_count
                                .localizedKeyWithFormat("\(Int(historyData.data.nbOkResponse()))"))
                            .foregroundColor(R.color.onSurface.color)
                            .font(R.font.robotoRegular.font(size: 16))
                            .padding([.leading, .trailing])
                        Spacer()
                        R.image.goDetail.image
                            .foregroundColor(R.color.primary.color)
                            .padding([.trailing])
                    }
                }
                .padding([.bottom, .top])
                Divider().padding(.leading, 65)
            }
            if historyData.data.nbKoResponse() > 0 {
                NavigationLink(value: QuestionListingScreen.route.init(historyId: Int(historyData.id),
                                                                       isSuccess: false)) {
                    HStack {
                        R.image.check_bad.image.foregroundColor(R.color.error.color)
                            .padding([.leading])
                        Text(R.string.localizable.result_bad_response_count
                                .localizedKeyWithFormat("\(Int(historyData.data.nbKoResponse()))"))
                            .foregroundColor(R.color.onSurface.color)
                            .font(R.font.robotoRegular.font(size: 16))
                            .padding([.leading, .trailing])
                        Spacer()
                        R.image.goDetail.image
                            .foregroundColor(R.color.primary.color)
                            .padding([.trailing])
                    }
                }
                .padding([.bottom, .top])
                Divider().padding(.leading, 65)
            }
        }.background {
            Color(uiColor: UIColor.systemBackground)
                .shadow(color: R.color.shadowColor.color.opacity(0.15),
                        radius: 4,
                        x: 0,
                        y: 7)
        }

        VStack {
            R.image.companyDetail.image
                .foregroundColor(R.color.primary.color)
            CustomAttributedText(stringRes: R.string.localizable.result_know_more.key,
                                 custom: [
                                    .ITALIC: .init(font: R.font.poppinsExtraBold.font(size: 20)),
                                    .DEFAULT: .init(font: R.font.poppinsExtraBold.font(size: 20))
                                 ])
                .multilineTextAlignment(.center)
        }
        .onTapGesture {
            goToCompanyDetail()
        }
        .padding()
        Spacer()
    }
}

#Preview {

    GameResultView(historyData: HistoryData.companion.dummy) {}
        .previewLayout(.sizeThatFits)
        .environmentObject(AppContext())
}
