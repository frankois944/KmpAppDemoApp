//
//  RoundedCorners.swift
//  CACDGAME
//
//  Created by François Dabonot on 19/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI

struct RoundedCorners: View {
    var background: Color
    var border: Color
    var topLeft: CGFloat = 0.0
    var topRight: CGFloat = 0.0
    var bottomLeft: CGFloat = 0.0
    var bottomRight: CGFloat = 0.0
    var lineWidth: Double = 1.0

    var body: some View {
        GeometryReader { geometry in
            Path { path in

                let width = geometry.size.width
                let height = geometry.size.height

                // Make sure we do not exceed the size of the rectangle
                let topRight = min(min(self.topRight, height/2), width/2)
                let topLeft = min(min(self.topLeft, height/2), width/2)
                let bottomLeft = min(min(self.bottomLeft, height/2), width/2)
                let bottomRight = min(min(self.bottomRight, height/2), width/2)

                path.move(to: CGPoint(x: width / 2.0, y: 0))
                path.addLine(to: CGPoint(x: width - topRight, y: 0))
                path.addArc(center: CGPoint(x: width - topRight, y: topRight),
                            radius: topRight,
                            startAngle: Angle(degrees: -90),
                            endAngle: Angle(degrees: 0), clockwise: false)
                path.addLine(to: CGPoint(x: width, y: height - bottomRight))
                path.addArc(center: CGPoint(x: width - bottomRight, y: height - bottomRight),
                            radius: bottomRight,
                            startAngle: Angle(degrees: 0),
                            endAngle: Angle(degrees: 90), clockwise: false)
                path.addLine(to: CGPoint(x: bottomLeft, y: height))
                path.addArc(center: CGPoint(x: bottomLeft, y: height - bottomLeft),
                            radius: bottomLeft,
                            startAngle: Angle(degrees: 90),
                            endAngle: Angle(degrees: 180),
                            clockwise: false)
                path.addLine(to: CGPoint(x: 0, y: topLeft))
                path.addArc(center: CGPoint(x: topLeft, y: topLeft),
                            radius: topLeft,
                            startAngle: Angle(degrees: 180),
                            endAngle: Angle(degrees: 270), clockwise: false)
                path.closeSubpath()
            }
            .fill(background, strokeBorder: border, lineWidth: lineWidth)
        }
    }
}
