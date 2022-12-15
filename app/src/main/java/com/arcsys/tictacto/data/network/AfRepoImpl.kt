package com.arcsys.tictacto.data.network

import android.content.Context
import android.util.Log
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.arcsys.tictacto.R

class AfRepoImpl : AfRepo {
    override fun getData(context: Context, map: (data: MutableMap<String, Any>?) -> Unit) {
        AppsFlyerLib.getInstance()
            .init(context.getString(R.string.af_key), object : AppsFlyerConversionListener {
                override fun onConversionDataSuccess(p0: MutableMap<String, Any>?) {
//                    map(p0)
                    Log.d("aaaa", "onConversionDataSuccess: ")
                    val mockAppsData: MutableMap<String, Any> = mutableMapOf()
                    mockAppsData["af_status"] = "Non-organic"
                    mockAppsData["media_source"] = "testSource"
                    mockAppsData["campaign"] = "test1_test2_test3_test4_test5"
                    mockAppsData["adset"] = "testAdset"
                    mockAppsData["adset_id"] = "testAdsetId"
                    mockAppsData["campaign_id"] = "testCampaignId"
                    mockAppsData["orig_cost"] = "1.22"
                    mockAppsData["af_site_id"] = "testSiteID"
                    mockAppsData["adgroup"] = "testAdgroup"
                    map(mockAppsData)
                }

                override fun onConversionDataFail(p0: String?) {
                    map(null)
                    Log.d("aaaa", "onConversionDataFail: ")
                }

                override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {}

                override fun onAttributionFailure(p0: String?) {}
            }, context)
        AppsFlyerLib.getInstance().start(context)
    }
}