package com.arcsys.tictacto.data.network

import android.content.Context

interface AfRepo {
    fun getData(context: Context, map: (data: MutableMap<String, Any>?) -> Unit)
}