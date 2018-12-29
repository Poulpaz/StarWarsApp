package com.example.lpiem.starwars.ui.activity

import androidx.appcompat.app.AppCompatActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.android.x.closestKodein

abstract class BaseActivity : AppCompatActivity(), KodeinAware {

    override val kodein by closestKodein()

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