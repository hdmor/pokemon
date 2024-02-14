package com.pokemon.go.pokemonlist

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.pokemon.go.models.PokemonListEntry
import com.pokemon.go.repository.PokemonRepository
import com.pokemon.go.utils.Constants
import com.pokemon.go.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(private val repository: PokemonRepository) : ViewModel() {

    private var currentPage = 0
    var pokemonList = mutableStateOf<List<PokemonListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReach = mutableStateOf(false)

    private var cachedPokemonList = listOf<PokemonListEntry>()
    private var isSearchStarting = true
    var isSearching = mutableStateOf(false)

    init {
        loadPokemonListPaginated()
    }

    fun searchInPokemonList(query: String) {
        val listToSearch = if (isSearchStarting) pokemonList.value else cachedPokemonList
        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty()) {
                pokemonList.value = cachedPokemonList
                isSearching.value = false
                isSearchStarting = true
                return@launch
            }
            val results = listToSearch.filter {
                it.pokemonName.contains(query.trim(), ignoreCase = true) || it.number.toString() == query.trim()
            }
            if (isSearchStarting) {
                cachedPokemonList = pokemonList.value
                isSearchStarting = false
            }
            pokemonList.value = results
            isSearching.value = true
        }
    }

    fun loadPokemonListPaginated() {

        viewModelScope.launch {

            isLoading.value = true
            when (val result = repository.getPokemonList(Constants.PAGE_SIZE, currentPage * Constants.PAGE_SIZE)) {

                is Resource.Success -> {
                    endReach.value = currentPage * Constants.PAGE_SIZE >= result.data!!.count
                    // extract id of each pokemon from the url of api
                    val pokemonEntries = result.data.results.mapIndexed { _, entry ->
                        val number =
                            if (entry.url.endsWith("/")) entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                            else entry.url.takeLastWhile { it.isDigit() }
                        val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$number.png"
                        PokemonListEntry(
                            entry.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                            url,
                            number.toInt()
                        )
                    }
                    currentPage++
                    loadError.value = ""
                    isLoading.value = false
                    pokemonList.value += pokemonEntries
                }

                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }

                is Resource.Loading -> {

                }
            }
        }
    }

    fun calcDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {

        val bitmap = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Palette.from(bitmap).generate {
            it?.dominantSwatch?.rgb.let { colorValue ->
                onFinish(Color(colorValue!!))
            }
        }
    }
}