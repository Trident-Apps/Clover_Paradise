package com.arcsys.tictacto.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.webkit.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.arcsys.tictacto.R
import com.arcsys.tictacto.util.getFromDs
import com.arcsys.tictacto.util.insertIntoDs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ViewForWebActivity : AppCompatActivity() {
    private val TAG = "aaa"
    lateinit var wView: WebView
    private var isMoved = true
    private var someCallback: ValueCallback<Array<Uri?>>? = null
    private lateinit var passedLink: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_for_web_ac_layout)
        passedLink = intent.getStringExtra(STRING_EXTRA)!!
        Log.d(TAG, "passe link is - $passedLink")
        val uploadImageResult =
            registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
                someCallback?.onReceiveValue(uris.toTypedArray())
            }
        wView = findViewById(R.id.view_web)
        wView.loadUrl(passedLink)
        initWebView(wView, CustomWebClient())
        wView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
            }

            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri?>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                someCallback = filePathCallback
                loadImage()
                return true
            }

            override fun onCreateWindow(
                view: WebView?,
                isDialog: Boolean,
                isUserGesture: Boolean,
                resultMsg: Message?
            ): Boolean {
                val newView = wView
                with(newView.settings) {
                    javaScriptEnabled = true
                    javaScriptCanOpenWindowsAutomatically = true
                    domStorageEnabled = true
                    setSupportMultipleWindows(true)
                    val viewTransport = resultMsg?.obj as WebView.WebViewTransport
                    viewTransport.webView = newView
                    newView.webViewClient = object : WebViewClient() {
                        @Deprecated("Deprecated in Java")
                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            url: String?
                        ): Boolean {
                            view?.loadUrl(url ?: "")
                            isMoved = true
                            return true
                        }
                    }
                }
                return true
            }
        }
    }

    override fun onBackPressed() {
        if (wView.canGoBack()) {
            wView.goBack()
        }
    }

    inner class CustomWebClient : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            isMoved = false
        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            if (!isMoved) {
                if (url == getString(R.string.url_tiny)) {
                    with(Intent(this@ViewForWebActivity, CheckerActivity::class.java)) {
                        startActivity(this)
                        this@ViewForWebActivity.finish()
                    }
                } else {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val savedData = getFromDs(this@ViewForWebActivity)
                        if (savedData.isNullOrEmpty()) {
                            insertIntoDs(url!!, this@ViewForWebActivity)
                            Log.d(TAG, "onPageFinished: url inserted $url")
                        }
                    }
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_CANCELED) {
            someCallback?.onReceiveValue(null)
            return
        } else if (resultCode == Activity.RESULT_OK) {
            if (someCallback == null) return
            someCallback!!.onReceiveValue(
                WebChromeClient.FileChooserParams.parseResult(
                    resultCode,
                    data
                )
            )
            someCallback = null
        }
    }

    private fun initWebView(
        webView: WebView,
        webViewClient: WebViewClient,
    ) {
        webView.webViewClient = webViewClient
        with(webView.settings) {
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            domStorageEnabled = true
            userAgentString = userAgentString.replace(OLD_VALUE, NEW_VALUE)
            loadWithOverviewMode = false
            CookieManager.getInstance().setAcceptCookie(true)
            CookieManager.getInstance().setAcceptThirdPartyCookies(wView, true)
        }
    }

    fun loadImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = TYPE
        startActivityForResult(
            Intent.createChooser(intent, CHOOSER), CODE
        )
    }

    companion object {
        const val STRING_EXTRA = "extra"
        const val OLD_VALUE = "wv"
        const val NEW_VALUE = ""
        const val CHOOSER = "Image Chooser"
        const val CODE = 1
        const val TYPE = "image/*"
    }
}