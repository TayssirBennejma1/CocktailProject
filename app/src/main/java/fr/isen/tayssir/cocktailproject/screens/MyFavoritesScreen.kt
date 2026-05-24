package fr.isen.tayssir.cocktailproject.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import fr.isen.tayssir.cocktailproject.managers.FavoritesManager

@Composable
fun MyFavoritesScreen(
    onDrinkClick: (String) -> Unit
) {
    val context = LocalContext.current

    var favorites by remember {
        mutableStateOf(FavoritesManager.getFavorites(context).toList())
    }

    LaunchedEffect(Unit) {
        favorites = FavoritesManager.getFavorites(context).toList()
    }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "My Favorites",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (favorites.isEmpty()) {
            Text("No favorite cocktail yet.")
        } else {
            LazyColumn {
                items(favorites) { favorite ->
                    val parts = favorite.split("|")
                    val drinkId = parts[0]
                    val drinkName = parts.getOrNull(1) ?: "Unknown cocktail"

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable {
                                onDrinkClick(drinkId)
                            }
                    ) {
                        Text(
                            text = drinkName,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}