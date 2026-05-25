package fr.isen.tayssir.cocktailproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import fr.isen.tayssir.cocktailproject.screens.CategoriesScreen
import fr.isen.tayssir.cocktailproject.screens.DetailCocktailScreen
import fr.isen.tayssir.cocktailproject.screens.DrinksScreen
import fr.isen.tayssir.cocktailproject.screens.MyFavoritesScreen
import fr.isen.tayssir.cocktailproject.ui.theme.CocktailProjectTheme
import fr.isen.tayssir.cocktailproject.ui.theme.PrimarySmooth
import fr.isen.tayssir.cocktailproject.ui.theme.SurfaceLight
import fr.isen.tayssir.cocktailproject.ui.theme.TextDark
import fr.isen.tayssir.cocktailproject.ui.theme.TextSecondary
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

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
}

@Composable
fun CocktailApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = SurfaceLight.copy(alpha = 0.98f),
                tonalElevation = 12.dp,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(SurfaceLight)
            ) {
                val items = listOf(
                    Triple("random", Icons.Default.Home, "Discover"),
                    Triple("categories", Icons.Default.List, "Categories"),
                    Triple("favorites", Icons.Default.Favorite, "Favorites")
                )

                items.forEach { item ->
                    val route = item.first
                    val icon = item.second
                    val label = item.third
                    val isSelected = currentRoute?.startsWith(route) == true

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            if (!isSelected) {
                                val finalRoute = if (route == "favorites") "favorites/${System.currentTimeMillis()}" else route
                                navController.navigate(finalRoute) {
                                    popUpTo("random") { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = { Icon(icon, contentDescription = label) },
                        label = {
                            Text(
                                text = label,
                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SurfaceLight,
                            selectedTextColor = PrimarySmooth,
                            indicatorColor = PrimarySmooth,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )
                }
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
                        val encodedCategory = URLEncoder.encode(category, StandardCharsets.UTF_8.toString())
                        navController.navigate("drinks/$encodedCategory")
                    }
                )
            }
            composable(
                route = "drinks/{category}",
                arguments = listOf(navArgument("category") { type = NavType.StringType })
            ) { backStackEntry ->
                val category = backStackEntry.arguments?.getString("category") ?: ""
                val decodedCategory = URLDecoder.decode(category, StandardCharsets.UTF_8.toString())
                DrinksScreen(
                    category = decodedCategory,
                    onDrinkClick = { drinkId -> navController.navigate("detail/$drinkId") }
                )
            }
            composable(
                route = "detail/{drinkId}",
                arguments = listOf(navArgument("drinkId") { type = NavType.StringType })
            ) { backStackEntry ->
                val drinkId = backStackEntry.arguments?.getString("drinkId")
                DetailCocktailScreen(drinkId = drinkId)
            }
            composable(
                route = "favorites/{refreshKey}",
                arguments = listOf(navArgument("refreshKey") { type = NavType.StringType })
            ) { backStackEntry ->
                val refreshKey = backStackEntry.arguments?.getString("refreshKey") ?: ""
                MyFavoritesScreen(
                    refreshKey = refreshKey,
                    onDrinkClick = { drinkId -> navController.navigate("detail/$drinkId") }
                )
            }
        }
    }
}
