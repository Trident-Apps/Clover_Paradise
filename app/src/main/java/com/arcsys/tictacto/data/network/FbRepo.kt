package com.arcsys.tictacto.data.network

import android.content.Context

interface FbRepo {
    fun getFbDeep(context: Context, string: (link: String) -> Unit)
}