package com.pokemon.go.di

import com.pokemon.go.data.remote.ApiService
import com.pokemon.go.repository.PokemonRepository
import com.pokemon.go.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesApiService(): ApiService =
        Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiService::class.java)

    @Singleton
    @Provides
    fun providesPokemonRepository(apiService: ApiService): PokemonRepository = PokemonRepository(apiService)

}