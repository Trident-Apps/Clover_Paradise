package com.arcsys.tictacto.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.arcsys.tictacto.R
import com.arcsys.tictacto.data.network.AfRepoImpl
import com.arcsys.tictacto.data.network.FbRepo
import com.arcsys.tictacto.data.network.OneSignalRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.moznion.uribuildertiny.URIBuilderTiny
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CloverVM @Inject constructor(
    private val fb: FbRepo,
    private val os: OneSignalRepo,
    app: Application
) : MainVM(app) {
    private val TAG = "aaa"
    private val af = AfRepoImpl()

    private val _afMap = MutableLiveData<MutableMap<String, Any>?>()
    val afMap: LiveData<MutableMap<String, Any>?> = _afMap
    private val _fbString = MutableLiveData<String>()
    val fbString: LiveData<String> = _fbString
    private val _advString = MutableLiveData<String>()
    val advString: LiveData<String> = _advString
    private val _urlMutableLiveData = MutableLiveData<String>()
    val urlLiveData: LiveData<String> = _urlMutableLiveData

    init {
        viewModelScope.launch {
            getAdvId()
        }
        getAfMap()
        getFbString()
        Log.d(TAG, "VM init block")
    }

    fun initUrl(
        sourceString: String,
        appsUUID: String,
        string: String,
        map: MutableMap<String, Any>?,
        advString: String,
        activityCtx: Context
    ) {
        _urlMutableLiveData.postValue(
            URIBuilderTiny("https://bananaclass.me")
                .appendPathsByString("cloverparadise.php")
                .addQueryParameter(
                    activityCtx.getString(R.string.sec_param),
                    activityCtx.getString(R.string.sec)
                )
                .addQueryParameter(activityCtx.getString(R.string.tmz), TimeZone.getDefault().id)
                .addQueryParameter(activityCtx.getString(R.string.advId), advString)
                .addQueryParameter(activityCtx.getString(R.string.fb), string)
                .addQueryParameter(activityCtx.getString(R.string.src), sourceString)
                .addQueryParameter(activityCtx.getString(R.string.apps_id), appsUUID)
                .addQueryParameter(
                    activityCtx.getString(R.string.adset_id),
                    map?.get("adset_id").toString()
                )
                .addQueryParameter(
                    activityCtx.getString(R.string.campaign),
                    map?.get("campaign_id").toString()
                )
                .addQueryParameter(
                    activityCtx.getString(R.string.app_campaign),
                    map?.get("campaign").toString()
                )
                .addQueryParameter(
                    activityCtx.getString(R.string.adset),
                    map?.get("adset").toString()
                )
                .addQueryParameter(
                    activityCtx.getString(R.string.adgroup),
                    map?.get("adgroup").toString()
                )
                .addQueryParameter(
                    activityCtx.getString(R.string.orig_cost),
                    map?.get("orig_cost").toString()
                )
                .addQueryParameter(
                    activityCtx.getString(R.string.af_siteid),
                    map?.get("af_siteid").toString()
                )
                .build().toString()
        )
    }


    private fun getFbString() {
        fb.getFbDeep(context) {
            _fbString.postValue(it)
        }
    }

    private fun getAfMap() {
        af.getData(context) {
            _afMap.postValue(it)
        }
    }

    private suspend fun getAdvId() = viewModelScope.launch(Dispatchers.IO) {
        _advString.postValue(os.getAdvertId(context))
    }

    suspend fun initOne(advString: String) = viewModelScope.launch(Dispatchers.IO) {
        os.initOneSignal(context, advString)
    }
}