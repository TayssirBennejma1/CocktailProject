package fr.isen.tayssir.cocktailproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import fr.isen.tayssir.cocktailproject.screens.CategoriesScreen
import fr.isen.tayssir.cocktailproject.screens.DetailCocktailScreen
import fr.isen.tayssir.cocktailproject.screens.DrinksScreen
import fr.isen.tayssir.cocktailproject.ui.theme.CocktailProjectTheme
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import fr.isen.tayssir.cocktailproject.screens.MyFavoritesScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CocktailProjectTheme {
                CocktailApp()
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

@Composable
fun CocktailApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate("random")
                    },
                    icon = {
                        Icon(Icons.Default.Home, contentDescription = "Random")
                    },
                    label = {
                        Text("Random")
                    }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate("categories")
                    },
                    icon = {
                        Icon(Icons.Default.List, contentDescription = "List")
                    },
                    label = {
                        Text("List")
                    }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate("favorites/${System.currentTimeMillis()}")
                    },
                    icon = {
                        Icon(Icons.Default.Favorite, contentDescription = "Favorites")
                    },
                    label = {
                        Text("Favorites")
                    }
                )
            }
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = "random",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("random") {
                DetailCocktailScreen()
            }

            composable("categories") {
                CategoriesScreen(
                    onCategoryClick = { category ->
                        val encodedCategory = URLEncoder.encode(
                            category,
                            StandardCharsets.UTF_8.toString()
                        )
                        navController.navigate("drinks/$encodedCategory")
                    }
                )
            }

            composable(
                route = "drinks/{category}",
                arguments = listOf(
                    navArgument("category") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                val category = backStackEntry.arguments?.getString("category") ?: ""
                val decodedCategory = URLDecoder.decode(
                    category,
                    StandardCharsets.UTF_8.toString()
                )

                DrinksScreen(
                    category = decodedCategory,
                    onDrinkClick = { drinkId ->
                        navController.navigate("detail/$drinkId")
                    }
                )
            }

            composable(
                route = "detail/{drinkId}",
                arguments = listOf(
                    navArgument("drinkId") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                val drinkId = backStackEntry.arguments?.getString("drinkId")
                DetailCocktailScreen(drinkId = drinkId)
            }

            composable(
                route = "favorites/{refreshKey}",
                arguments = listOf(
                    navArgument("refreshKey") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->

                val refreshKey = backStackEntry.arguments?.getString("refreshKey") ?: ""

                MyFavoritesScreen(
                    refreshKey = refreshKey,
                    onDrinkClick = { drinkId ->
                        navController.navigate("detail/$drinkId")
                    }
                )
            }
        }
    }
}

