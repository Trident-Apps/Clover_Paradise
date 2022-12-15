package com.arcsys.tictacto.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class MainVM @Inject constructor(app: Application) : AndroidViewModel(app) {
    protected val context get() = getApplication<Application>()
}