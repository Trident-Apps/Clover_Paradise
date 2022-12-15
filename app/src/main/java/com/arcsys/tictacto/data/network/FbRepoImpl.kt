package com.arcsys.tictacto.data.network

import android.content.Context
import android.util.Log
import com.facebook.applinks.AppLinkData

class FbRepoImpl : FbRepo {
    override fun getFbDeep(context: Context, string: (link: String) -> Unit) {
        AppLinkData.fetchDeferredAppLinkData(context) {
            string(it?.targetUri.toString())
//            string("myapp://test1/test2/test3/test4/test5")
            Log.d("aaa", "getFbDeep: ")
        }
    }
}