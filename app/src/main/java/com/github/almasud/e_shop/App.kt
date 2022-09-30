package com.github.almasud.e_shop

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {

    object Constant {
        object Api {
            const val BASE_URL: String = "https://devapiv2.walcart.com/graphql"
        }
    }
}