package com.bryant.videoplayerdemo.log

import timber.log.Timber

class MultiTagTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement) =
        "Bryant-(${element.fileName}:${element.lineNumber})#${element.methodName}"
}