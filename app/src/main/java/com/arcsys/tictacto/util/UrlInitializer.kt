package com.arcsys.tictacto.util

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arcsys.tictacto.R
import net.moznion.uribuildertiny.URIBuilderTiny
import java.util.TimeZone

class UrlInitializer {
    private val _urlMutableLiveData = MutableLiveData<MutableMap<String, Any>?>()
    val urlLiveData: LiveData<MutableMap<String, Any>?> = _urlMutableLiveData
    fun initUrl(
        sourceString: String,
        appsUUID: String,
        string: String,
        map: MutableMap<String, Any>?,
        advString: String,
        activityCtx: Context
    ): String {
        val secureKeyMap = mapOf<String, Any>(
            activityCtx.getString(R.string.sec_param) to activityCtx.getString(R.string.sec)
        )
        val timeZoneMap = mapOf<String, Any>(
            activityCtx.getString(R.string.tmz) to TimeZone.getDefault().id
        )
        val gadIdMap = mapOf<String, Any>(
            activityCtx.getString(R.string.advId) to advString
        )
        val deepLinkMap = mapOf<String, Any>(
            activityCtx.getString(R.string.fb) to string
        )
        val sourceMap = mapOf(
            activityCtx.getString(R.string.src) to sourceString
        )
        val afIdMap = mapOf(
            activityCtx.getString(R.string.af_key) to appsUUID
        )
        val adSetIdMap = mapOf(
            activityCtx.getString(R.string.adset_id) to map?.get("adset_id")
        )
        val campaignIdMap = mapOf(
            activityCtx.getString(R.string.campaign) to map?.get("campaign_id")
        )
        val appCampaignMap = mapOf(
            activityCtx.getString(R.string.app_campaign) to map?.get("campaign")
        )
        val adSetMap = mapOf(
            activityCtx.getString(R.string.adset) to map?.get("adset")
        )
        val adGroupMap = mapOf(
            activityCtx.getString(R.string.adgroup) to map?.get("adgroup")
        )
        val origCostMap = mapOf(
            activityCtx.getString(R.string.orig_cost) to map?.get("orig_cost")
        )
        val afSiteIdMap = mapOf(
            activityCtx.getString(R.string.af_siteid) to map?.get("af_siteid")
        )
        val tinyUrl = URIBuilderTiny("https://bananaclass.me").apply {
            appendPathsByString("cloverparadise.php")
            setQueryParameters(secureKeyMap)
            setQueryParameters(timeZoneMap)
            setQueryParameters(gadIdMap)
            setQueryParameters(deepLinkMap)
            setQueryParameters(sourceMap)
            setQueryParameters(afIdMap)
            setQueryParameters(adSetIdMap)
            setQueryParameters(campaignIdMap)
            setQueryParameters(appCampaignMap)
            setQueryParameters(adSetMap)
            setQueryParameters(adGroupMap)
            setQueryParameters(origCostMap)
            setQueryParameters(afSiteIdMap)
        }.build()
        return tinyUrl.toString()
    }
}