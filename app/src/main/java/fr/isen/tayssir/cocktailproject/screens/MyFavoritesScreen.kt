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
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun MyFavoritesScreen(
    refreshKey: String,
    onDrinkClick: (String) -> Unit
) {
    val context = LocalContext.current

    var favorites by remember {
        mutableStateOf(FavoritesManager.getFavorites(context).toList())
    }

    LaunchedEffect(refreshKey) {
        favorites = FavoritesManager.getFavorites(context).toList()
    }

    Column(
        modifier = Modifier
            .background(Color(0xFFF8F7FB))
            .padding(16.dp)
    ) {
        Text(
            text = "My Favorite Cocktails",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2B2B2B)
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
                            .padding(vertical = 7.dp)
                            .clickable {
                                onDrinkClick(drinkId)
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFEDEAF5)
                        )
                    ) {
                        Text(
                            text = drinkName,
                            modifier = Modifier.padding(18.dp),
                            color = Color(0xFF333333)
                        )
                    }
                }
            }
        }
    }
}