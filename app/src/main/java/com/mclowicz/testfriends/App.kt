package com.mclowicz.testfriends

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application()

fun main(args: Array<String>) {
    var tvShows = "many"
    tvShows = "too many"
    println(tvShows)
}