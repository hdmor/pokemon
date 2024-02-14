package com.pokemon.go

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pokemon.go.pokemoninfo.PokemonInfoScreen
import com.pokemon.go.pokemonlist.PokemonListScreen
import com.pokemon.go.ui.theme.PokemonTheme
import com.pokemon.go.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokemonTheme {

                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Constants.POKEMON_LIST_SCREEN) {

                    composable(route = Constants.POKEMON_LIST_SCREEN) {
                        PokemonListScreen(navController = navController)
                    }

                    composable(
                        route = "${Constants.POKEMON_INFO_SCREEN}/{dominantColor}/{pokemonNumber}",
                        arguments = listOf(
                            navArgument("dominantColor") {
                                type = NavType.IntType
                            },
                            navArgument("pokemonNumber") {
                                type = NavType.IntType
                            }
                        )
                    ) {
                        val dominantColor = remember { it.arguments?.getInt("dominantColor")?.let { Color(it) } }
                        val pokemonNumber = remember { it.arguments?.getInt("pokemonNumber") }
                        PokemonInfoScreen(
                            dominantColor = dominantColor ?: Color.Black,
                            pokemonNumber = pokemonNumber ?: 0,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}