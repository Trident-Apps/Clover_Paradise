package com.arcsys.tictacto.data.network

import android.content.Context
import android.util.Log
import com.arcsys.tictacto.R
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.onesignal.OneSignal

class OneSignalRepoImpl : OneSignalRepo {
    override suspend fun getAdvertId(context: Context): String {
        return AdvertisingIdClient.getAdvertisingIdInfo(context).id.toString()
    }

    override suspend fun initOneSignal(context: Context, advId: String) {
        OneSignal.initWithContext(context)
        OneSignal.setAppId(context.getString(R.string.onesignal_id))
        OneSignal.setExternalUserId(advId)
        Log.d("aaa", "initOneSignal: ")
    }
}