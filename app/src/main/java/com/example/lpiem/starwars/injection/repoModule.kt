package com.example.lpiem.starwars.injection

import com.example.lpiem.starwars.repository.CardsRepository
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val repoModule = Kodein.Module("RepoModule") {
    bind<CardsRepository>() with singleton { CardsRepository(instance()) }
}