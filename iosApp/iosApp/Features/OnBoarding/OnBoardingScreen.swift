//
//  OnBoardingScreen.swift
//  CACDGAME
//
//  Created by François Dabonot on 06/07/2023.
//  Copyright © 2023 CACD2. All rights reserved.
//

import SwiftUI

struct OnBoardingScreen: View {

    static let route = OnBoardingModel.self

    @State var pageIndex = 0
    @FocusState var focusedUsernameField: Bool
    var onGoToSelectionScreen: (String) -> Void

    var body: some View {
        NavigationStack {
            TabView(selection: $pageIndex) {
                OnBoardingPage1View {
                    pageIndex+=1
                }
                .tag(0)
                .analyticsScreen(name: "OnBoardingPage1View",
                                 class: "OnBoardingPage1View")
                OnBoardingPage2View(onNextPage: {
                    pageIndex+=1
                }, onPrevPage: {
                    pageIndex-=1
                })
                .tag(1)
                .analyticsScreen(name: "OnBoardingPage2View",
                                 class: "OnBoardingPage3View")
                OnBoardingPage3View(onNextPage: {
                    pageIndex+=1
                }, onPrevPage: {
                    pageIndex-=1
                })
                .tag(2)
                .analyticsScreen(name: "OnBoardingPage3View",
                                 class: "OnBoardingPage3View")
                OnBoardingPage4View(focusedUsernameField: _focusedUsernameField,
                                    onNextPage: { username in
                                        onGoToSelectionScreen(username)
                                    }, onPrevPage: {
                                        pageIndex-=1
                                    })
                    .tag(3)
                    .analyticsScreen(name: "OnBoardingPage4View",
                                     class: "OnBoardingPage4View")
            }
            .tabViewStyle(.page(indexDisplayMode: .never))
            .onChange(of: pageIndex) { currentPage in
                focusedUsernameField = currentPage == 3
            }.toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    R.image.small_logo.image
                }
            }
        }
    }
}

#Preview {
    OnBoardingScreen(onGoToSelectionScreen: { _ in })
        .environmentObject(AppContext())
}
