package fr.isen.tayssir.cocktailproject.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import fr.isen.tayssir.cocktailproject.data.Cocktail
import fr.isen.tayssir.cocktailproject.managers.FavoritesManager
import fr.isen.tayssir.cocktailproject.network.ApiClient


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailCocktailScreen(
    drinkId: String? = null
) {
    val context = LocalContext.current

    var cocktail by remember { mutableStateOf<Cocktail?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(drinkId) {
        isLoading = true

        cocktail = if (drinkId == null) {
            ApiClient.apiService.getRandomCocktail().drinks?.firstOrNull()
        } else {
            ApiClient.apiService.getCocktailById(drinkId).drinks?.firstOrNull()
        }

        cocktail?.let {
            isFavorite = FavoritesManager.isFavorite(context, it.id)
        }

        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Cocktail Details")
                },
                actions = {
                    IconButton(
                        onClick = {
                            cocktail?.let {
                                FavoritesManager.toggleFavorite(context, it.id, it.name)
                                isFavorite = FavoritesManager.isFavorite(context, it.id)

                                Toast.makeText(
                                    context,
                                    if (isFavorite) "Added to favorites" else "Removed from favorites",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorite"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        if (isLoading) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                CircularProgressIndicator()
            }
        } else {
            cocktail?.let { item ->

                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    AsyncImage(
                        model = item.image,
                        contentDescription = item.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Category: ${item.category ?: "Unknown"}")
                    Text(text = "Type: ${item.alcoholic ?: "Unknown"}")
                    Text(text = "Glass: ${item.glass ?: "Unknown"}")

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Ingredients",
                                style = MaterialTheme.typography.titleLarge
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            item.ingredientsList().forEach { ingredient ->
                                Text(text = "- $ingredient")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Recipe",
                                style = MaterialTheme.typography.titleLarge
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(text = item.instructions ?: "No recipe available")
                        }
                    }
                }
            }
        }
    }
}