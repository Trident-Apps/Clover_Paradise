package com.arcsys.tictacto.data.network

import android.content.Context

interface OneSignalRepo {
    suspend fun getAdvertId(context: Context): String

    suspend fun initOneSignal(context: Context, advId: String)
}