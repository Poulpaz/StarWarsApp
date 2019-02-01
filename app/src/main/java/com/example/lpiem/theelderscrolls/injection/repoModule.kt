package com.example.lpiem.theelderscrolls.injection

import com.example.lpiem.theelderscrolls.repository.CardsRepository
import com.example.lpiem.theelderscrolls.repository.UserRepository
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val repoModule = Kodein.Module("RepoModule") {
    bind<CardsRepository>() with singleton { CardsRepository(instance()) }
    bind<UserRepository>() with singleton { UserRepository(instance(), instance(), instance()) }
}