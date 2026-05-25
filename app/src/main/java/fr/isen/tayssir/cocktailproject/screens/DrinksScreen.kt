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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import fr.isen.tayssir.cocktailproject.data.DrinkPreview
import fr.isen.tayssir.cocktailproject.network.ApiClient
import fr.isen.tayssir.cocktailproject.ui.components.AppBackground
import fr.isen.tayssir.cocktailproject.ui.components.CenterLoading
import fr.isen.tayssir.cocktailproject.ui.components.EmptyState
import fr.isen.tayssir.cocktailproject.ui.components.PremiumCard
import fr.isen.tayssir.cocktailproject.ui.components.ScreenHeader
import fr.isen.tayssir.cocktailproject.ui.components.SmallStat
import fr.isen.tayssir.cocktailproject.ui.theme.PrimarySmooth
import fr.isen.tayssir.cocktailproject.ui.theme.SecondarySmooth
import fr.isen.tayssir.cocktailproject.ui.theme.SurfaceLight
import fr.isen.tayssir.cocktailproject.ui.theme.TextDark
import fr.isen.tayssir.cocktailproject.ui.theme.TextSecondary

@Composable
fun DrinksScreen(
    category: String,
    onDrinkClick: (String) -> Unit
) {
    var drinks by remember { mutableStateOf<List<DrinkPreview>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }
    var search by remember { mutableStateOf("") }

    LaunchedEffect(category) {
        isLoading = true
        hasError = false
        try {
            drinks = ApiClient.apiService.getDrinksByCategory(category).drinks ?: emptyList()
        } catch (e: Exception) {
            hasError = true
        }
        isLoading = false
    }

    val filteredDrinks = remember(drinks, search) {
        if (search.isBlank()) drinks else drinks.filter { it.name.contains(search, ignoreCase = true) }
    }

    AppBackground(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            ScreenHeader(
                title = category,
                subtitle = "Découvre les meilleures recettes de cette catégorie."
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SmallStat(value = drinks.size.toString(), label = "cocktails", modifier = Modifier.weight(1f))
                SmallStat(value = filteredDrinks.size.toString(), label = "résultats", modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("Rechercher un cocktail") },
                shape = RoundedCornerShape(22.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimarySmooth,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = SurfaceLight,
                    unfocusedContainerColor = SurfaceLight,
                    cursorColor = PrimarySmooth
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            when {
                isLoading -> CenterLoading()
                hasError -> EmptyState("⚠️", "Chargement impossible", "Vérifie ta connexion puis réessaie.")
                filteredDrinks.isEmpty() -> EmptyState("🔎", "Aucun résultat", "Essaie avec un autre nom de cocktail.")
                else -> LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(filteredDrinks) { drink ->
                        DrinkItem(drink = drink, onClick = { onDrinkClick(drink.id) })
                    }
                }
            }
        }
    }
}

@Composable
private fun DrinkItem(drink: DrinkPreview, onClick: () -> Unit) {
    PremiumCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(24.dp))
            ) {
                AsyncImage(
                    model = drink.image,
                    contentDescription = drink.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Black.copy(alpha = 0.32f))
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = drink.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextDark,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Voir la recette complète",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }

            Surface(
                shape = RoundedCornerShape(50.dp),
                color = SecondarySmooth.copy(alpha = 0.22f)
            ) {
                Text(
                    text = "Open",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = PrimarySmooth,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}
