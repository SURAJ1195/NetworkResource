package com.example.mynetworkcall

import android.content.Context

interface OuterIntentInterface {
    fun <T> startActivity(
        context: Context,
        packageName: String,
        activityName: String,
        dataToIntent: List<Pair<String, T?>?>?
    )
}