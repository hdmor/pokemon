package com.pokemon.go.pokemoninfo

import androidx.lifecycle.ViewModel
import com.pokemon.go.data.remote.responses.PokemonInfo
import com.pokemon.go.repository.PokemonRepository
import com.pokemon.go.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonInfoViewModel @Inject constructor(private val repository: PokemonRepository) : ViewModel() {

    suspend fun getPokemonInfo(number: Int): Resource<PokemonInfo> = repository.getPokemonInfo(number)

}