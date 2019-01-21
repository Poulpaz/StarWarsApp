package com.example.lpiem.theelderscrolls.injection

import android.content.Context
import android.content.SharedPreferences
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val preferenceModule = Kodein.Module("PreferenceModule") {
    val preferenceFileKey = "com.theelderscrolls.preference_file_key"

    bind<SharedPreferences>() with singleton {
        instance<Context>().getSharedPreferences(preferenceFileKey, Context.MODE_PRIVATE)
    }
}