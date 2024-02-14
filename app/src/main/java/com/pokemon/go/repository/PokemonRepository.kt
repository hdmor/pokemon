package com.pokemon.go.repository

import com.pokemon.go.data.remote.ApiService
import com.pokemon.go.data.remote.responses.PokemonInfo
import com.pokemon.go.data.remote.responses.PokemonList
import com.pokemon.go.utils.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PokemonRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getPokemonList(limit: Int, offset: Int): Resource<PokemonList> {

        val response = try {
            apiService.getPokemonList(limit, offset)
        } catch (exception: Exception) {
            return Resource.Error(exception.message)
        }
        return Resource.Success(response)
    }

    suspend fun getPokemonInfo(number: Int): Resource<PokemonInfo> {
        val response = try {
            apiService.getPokemonInfo(number)
        } catch (exception: Exception) {
            return Resource.Error(exception.message)
        }
        return Resource.Success(response)
    }
}