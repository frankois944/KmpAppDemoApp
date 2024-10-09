//
//  GameHistoryItemView.swift
//  CACDGAME
//
//  Created by François Dabonot on 12/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import Shared

struct GameHistoryItemView: View {

    let item: HistoryData

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            HStack(alignment: .top) {
                GameResultChartView(okData: item.data.nbOkResponse(),
                                    koData: item.data.nbKoResponse(),
                                    fontSizeCorrectQuestion: 13,
                                    fontSizeTotalQuestion: 10,
                                    withIcons: false,
                                    size: .init(width: 70, height: 70))
                    .frame(maxWidth: 70, maxHeight: 70)
                    .aspectRatio(1, contentMode: .fit)
                VStack(alignment: .leading) {
                    Text(item.category.title())
                        .foregroundColor(R.color.onSurface.color)
                        .font(R.font.poppinsMedium.font(size: 14))
                    Text(R.string.localizable.history_description.localizedKey)
                        .foregroundColor(R.color.onSurface.color)
                        .font(R.font.poppinsLight.font(size: 10))
                        .lineSpacing(3)
                }
                Spacer()
            }
            .padding([.leading, .top, .trailing], 13)
            Divider()
                .padding(.leading, 20)
                .padding(.trailing, 20)
                .padding(.top, 4)
                .padding(.bottom, 12)
            HStack(spacing: 30) {
                HStack(spacing: 10) {
                    Text(item.category.label())
                        .font(R.font.robotoMedium.font(size: 10))
                        .tracking(0.40)
                        .lineSpacing(16)
                        .foregroundColor(R.color.onPrimary.color)
                        .fixedSize(horizontal: true, vertical: false)
                }
                .padding(EdgeInsets(top: 4, leading: 4, bottom: 4, trailing: 4))
                .frame(width: 45, height: 15)
                .background(R.color.primary.color.opacity(0.38))
                .cornerRadius(2)
                Text(Date(timeIntervalSince1970: TimeInterval(item.timestamp / 1000))
                        .formatted(date: .abbreviated,
                                   time: .omitted))
                    .font(R.font.poppinsLight.font(size: 10))
                    .foregroundColor(R.color.onSurface.color)
                Spacer()
            }
            .padding(.leading, 18)
            .padding(.bottom, 10)
        }
        .background(
            RoundedCorners(background: R.color.surface.color, border: .clear,
                           topLeft: 16,
                           topRight: 16,
                           bottomLeft: 16,
                           bottomRight: 16)
                .shadow(color: R.color.onSurface.color.opacity(0.15), radius: 6, x: 1, y: 4)
        )
    }
}

#Preview {
    GameHistoryItemView(item: HistoryData.companion.dummy)
        .previewLayout(.sizeThatFits)
        .environmentObject(AppContext())
}
