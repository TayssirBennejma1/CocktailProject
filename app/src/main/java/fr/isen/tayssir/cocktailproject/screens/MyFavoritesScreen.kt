package fr.isen.tayssir.cocktailproject.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fr.isen.tayssir.cocktailproject.managers.FavoritesManager
import fr.isen.tayssir.cocktailproject.ui.components.AppBackground
import fr.isen.tayssir.cocktailproject.ui.components.EmptyState
import fr.isen.tayssir.cocktailproject.ui.components.PremiumCard
import fr.isen.tayssir.cocktailproject.ui.components.ScreenHeader
import fr.isen.tayssir.cocktailproject.ui.components.SmallStat
import fr.isen.tayssir.cocktailproject.ui.theme.PrimarySmooth
import fr.isen.tayssir.cocktailproject.ui.theme.SecondarySmooth
import fr.isen.tayssir.cocktailproject.ui.theme.SoftPurple
import fr.isen.tayssir.cocktailproject.ui.theme.TextDark
import fr.isen.tayssir.cocktailproject.ui.theme.TextSecondary

@Composable
fun MyFavoritesScreen(
    refreshKey: String,
    onDrinkClick: (String) -> Unit
) {
    val context = LocalContext.current
    var favorites by remember { mutableStateOf(FavoritesManager.getFavorites(context).toList()) }

    LaunchedEffect(refreshKey) {
        favorites = FavoritesManager.getFavorites(context).toList()
    }

    AppBackground(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            ScreenHeader(
                title = "Favorites",
                subtitle = "Tes cocktails sauvegardés pour les retrouver rapidement."
            )

            Spacer(modifier = Modifier.height(16.dp))
            SmallStat(value = favorites.size.toString(), label = "favoris", modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(18.dp))

            if (favorites.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyState("🤍", "Aucun favori", "Ajoute un cocktail avec le bouton cœur pour le retrouver ici.")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(favorites) { favorite ->
                        val parts = favorite.split("|")
                        val drinkId = parts[0]
                        val drinkName = parts.getOrNull(1) ?: "Unknown cocktail"
                        FavoriteItem(
                            name = drinkName,
                            onClick = { onDrinkClick(drinkId) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoriteItem(name: String, onClick: () -> Unit) {
    PremiumCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(72.dp),
                shape = RoundedCornerShape(24.dp),
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                listOf(PrimarySmooth, SecondarySmooth, SoftPurple)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        modifier = Modifier.size(44.dp),
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.24f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, end = 8.dp)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextDark,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Ouvrir la recette",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }

            Text(
                text = "→",
                style = MaterialTheme.typography.headlineMedium,
                color = PrimarySmooth,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}
