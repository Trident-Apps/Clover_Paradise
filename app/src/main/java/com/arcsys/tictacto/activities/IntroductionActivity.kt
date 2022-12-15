package com.arcsys.tictacto.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class IntroductionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navBetween(provideIntent(this), this)
    }

    private fun navBetween(intent: Intent, activity: Activity) {
        startActivity(intent)
        activity.finish()
    }

    private fun provideIntent(activity: Activity): Intent {
        return Intent(activity, CheckerActivity::class.java)
    }
}