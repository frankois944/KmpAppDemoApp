//
//  CustomRoundCorner.swift
//  CACDGAME
//
//  Created by François Dabonot on 17/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import Foundation
import SwiftUI

extension Shape {
    /// fills and strokes a shape
    public func fill<S: ShapeStyle>(
        _ fillContent: S,
        stroke: StrokeStyle
    ) -> some View {
        ZStack {
            self.fill(fillContent)
            self.stroke(style: stroke)
        }
    }
}

struct RoundedCorner: Shape {

    var radius: CGFloat = .infinity
    var corners: UIRectCorner = .allCorners

    func path(in rect: CGRect) -> Path {
        let path = UIBezierPath(roundedRect: rect,
                                byRoundingCorners: corners,
                                cornerRadii: CGSize(width: radius, height: radius))
        return Path(path.cgPath)
    }
}

extension Shape {
    func fill<Fill: ShapeStyle, Stroke: ShapeStyle>(_ fillStyle: Fill,
                                                    strokeBorder strokeStyle: Stroke,
                                                    lineWidth: Double = 1) -> some View {
        self
            .stroke(strokeStyle, lineWidth: lineWidth)
            .background(self.fill(fillStyle))
    }
}

extension InsettableShape {
    func fill<Fill: ShapeStyle, Stroke: ShapeStyle>(_ fillStyle: Fill,
                                                    strokeBorder strokeStyle: Stroke,
                                                    lineWidth: Double = 1) -> some View {
        self
            .strokeBorder(strokeStyle, lineWidth: lineWidth)
            .background(self.fill(fillStyle))
    }
}

struct RoundCorner: Shape {

    // MARK: - PROPERTIES

    var cornerRadius: CGFloat
    var maskedCorners: UIRectCorner

    // MARK: - PATH

    func path(in rect: CGRect) -> Path {
        let path = UIBezierPath(roundedRect: rect,
                                byRoundingCorners: maskedCorners,
                                cornerRadii: CGSize(width: cornerRadius, height: cornerRadius))
        return Path(path.cgPath)
    }
}

// MARK: - PREVIEW

#Preview {
    RoundCorner(cornerRadius: 20, maskedCorners: .allCorners)
}
