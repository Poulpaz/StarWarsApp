package com.example.lpiem.starwars.injection

import com.example.lpiem.starwars.repository.StarshipsRepository
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

val repoModule = Kodein.Module("RepoModule") {
    bind<StarshipsRepository>() with singleton { StarshipsRepository(instance()) }
}