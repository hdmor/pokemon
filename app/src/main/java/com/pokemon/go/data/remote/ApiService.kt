package com.pokemon.go.data.remote

import com.pokemon.go.data.remote.responses.PokemonInfo
import com.pokemon.go.data.remote.responses.PokemonList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("pokemon")
    suspend fun getPokemonList(@Query("limit") limit: Int, @Query("offset") offset: Int): PokemonList

    @GET("pokemon/{number}")
    suspend fun getPokemonInfo(@Path("number") number: Int): PokemonInfo
}