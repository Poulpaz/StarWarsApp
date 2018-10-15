package com.example.lpiem.starwars.View

import android.support.v7.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}