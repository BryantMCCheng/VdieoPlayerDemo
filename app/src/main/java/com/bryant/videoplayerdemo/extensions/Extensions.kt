package com.bryant.videoplayerdemo.extensions

import android.content.Context
import com.bryant.videoplayerdemo.App

val Any.TAG: String
    get() {
        val tag = "Bryant-${javaClass.simpleName}"
        return if (tag.length <= 23) tag else tag.substring(0, 23)
    }

val AppContext: Context
    get() = App.instance.applicationContext

val String.getVideo: String
    get() = "http://storage.googleapis.com/pst-framy/vdo/$this.mp4"

val String.getPreview: String
    get() = "http://storage.googleapis.com/pst-framy/stk/$this.jpg"

val String.getIcon: String
    get() = "http://storage.googleapis.com/usr-framy/headshot/$this.jpg"