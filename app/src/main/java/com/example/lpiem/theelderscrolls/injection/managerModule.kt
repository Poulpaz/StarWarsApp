package com.example.lpiem.theelderscrolls.injection

import com.example.lpiem.theelderscrolls.manager.GoogleConnectionManager
import com.example.lpiem.theelderscrolls.manager.KeystoreManager
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val managerModule = Kodein.Module("ManagerModule") {
    bind<KeystoreManager>() with singleton { KeystoreManager(instance()) }
    bind<GoogleConnectionManager>() with singleton { GoogleConnectionManager(instance()) }
}