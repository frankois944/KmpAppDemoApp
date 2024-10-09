//
//  AnswerDefinitionContentView.swift
//  CACDGAME
//
//  Created by François Dabonot on 24/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI
import Shared
import NukeUI
import Nuke

struct AnswerDefinitionContentView: View {

    let question: QuestionChoice
    let canGoToNextQuestion: Bool
    var imageRequest: ImageRequest?
    let isCachedIllustration: Bool

    @State var imageLoaded: Bool

    init(question: QuestionChoice, canGoToNextQuestion: Bool, imageLoaded: Bool = false) {
        self.question = question
        self.canGoToNextQuestion = canGoToNextQuestion
        self.imageLoaded = imageLoaded
        if let illustration = question.illustration {
            let url = URL(string: illustration)
            imageRequest = ImageRequest(url: url)
            isCachedIllustration = ImagePipeline.shared.cache.containsCachedImage(for: .init(url: url))
        } else {
            isCachedIllustration = true
        }
    }

    var body: some View {
        VStack {
            if let imageRequest = imageRequest {
                LazyImage(request: imageRequest,
                          content: { state in
                            if let image = state.image {
                                AnswerImageDefinitionContentView(image: image, custom: true)
                            } else if state.error != nil {
                                AnswerImageDefinitionContentView(image: question.image(), custom: false)
                            } else {
                                ProgressView()
                                    .progressViewStyle(CircularProgressViewStyle())
                                    .frame(height: 100)
                            }
                          })
                    .processors([.resize(height: 100)])
                    .if(isCachedIllustration, false: {
                        $0.animation(.default)
                    })
                    .padding(.bottom, 25)
            } else {
                AnswerImageDefinitionContentView(image: question.image(), custom: false)
                    .padding(.bottom, 25)
            }
            Text(question.content)
                .foregroundColor(R.color.primary.color)
                .font(R.font.poppinsExtraBold.font(size: 20))
                .multilineTextAlignment(.center)
                .fixedSize(horizontal: false, vertical: true)
                .padding(.leading)
                .padding(.trailing)
                .padding(.bottom, 25)
            Text(question.detail ?? "")
                .foregroundColor(R.color.onSurface.color)
                .font(R.font.poppinsMedium.font(size: 15))
                .fixedSize(horizontal: false, vertical: true)
                .padding(.leading)
                .padding(.trailing)
        }
        .padding([.leading, .trailing], 16)
        .padding([.top], 16)
        .padding(.bottom, canGoToNextQuestion ? 80 : 16)
        .scrollOnOverflow()
    }
}

struct AnswerImageDefinitionContentView: View {

    let image: Image
    let custom: Bool

    var body: some View {
        image
            .resizable()
            .if(custom, true: { img in
                return img.scaledToFit()
            })
            .if(!custom, true: { img in
                return img.scaledToFill()
            })
            .frame(height: 100)
            .clipped()
            .clipShape(RoundCorner(cornerRadius: 11, maskedCorners: .topLeft))
            .clipShape(RoundCorner(cornerRadius: 4, maskedCorners: .topRight))
            .clipShape(RoundCorner(cornerRadius: 4, maskedCorners: .bottomLeft))
            .clipShape(RoundCorner(cornerRadius: 11, maskedCorners: .bottomRight))
            .padding(.leading)
            .padding(.trailing)
    }
}

#Preview {
    AnswerDefinitionContentView(question: QuestionChoice.companion.dummy, canGoToNextQuestion: false)
        .environmentObject(AppContext(username: "DEBUG"))
}
