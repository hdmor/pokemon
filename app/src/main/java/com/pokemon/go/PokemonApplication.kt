package com.pokemon.go

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.request.CachePolicy
import coil.util.DebugLogger
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class PokemonApplication : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }

    override fun newImageLoader(): ImageLoader =
        ImageLoader(this).newBuilder().memoryCachePolicy(CachePolicy.ENABLED).logger(DebugLogger()).build()

}