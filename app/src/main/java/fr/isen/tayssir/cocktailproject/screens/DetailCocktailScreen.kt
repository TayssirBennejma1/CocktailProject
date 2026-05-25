package fr.isen.tayssir.cocktailproject.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import fr.isen.tayssir.cocktailproject.data.Cocktail
import fr.isen.tayssir.cocktailproject.managers.FavoritesManager
import fr.isen.tayssir.cocktailproject.network.ApiClient
import fr.isen.tayssir.cocktailproject.ui.components.AppBackground
import fr.isen.tayssir.cocktailproject.ui.components.CenterLoading
import fr.isen.tayssir.cocktailproject.ui.components.EmptyState
import fr.isen.tayssir.cocktailproject.ui.components.GradientPill
import fr.isen.tayssir.cocktailproject.ui.components.PremiumCard
import fr.isen.tayssir.cocktailproject.ui.theme.AccentGold
import fr.isen.tayssir.cocktailproject.ui.theme.PrimarySmooth
import fr.isen.tayssir.cocktailproject.ui.theme.SecondarySmooth
import fr.isen.tayssir.cocktailproject.ui.theme.SurfaceLight
import fr.isen.tayssir.cocktailproject.ui.theme.TextDark
import fr.isen.tayssir.cocktailproject.ui.theme.TextSecondary

@Composable
fun DetailCocktailScreen(
    drinkId: String? = null
) {
    val context = LocalContext.current
    var cocktail by remember { mutableStateOf<Cocktail?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(false) }
    var randomReload by remember { mutableIntStateOf(0) }

    LaunchedEffect(drinkId, randomReload) {
        isLoading = true
        hasError = false
        try {
            cocktail = if (drinkId == null) {
                ApiClient.apiService.getRandomCocktail().drinks?.firstOrNull()
            } else {
                ApiClient.apiService.getCocktailById(drinkId).drinks?.firstOrNull()
            }
            cocktail?.let { isFavorite = FavoritesManager.isFavorite(context, it.id) }
        } catch (e: Exception) {
            hasError = true
        }
        isLoading = false
    }

    Scaffold(containerColor = Color.Transparent) { paddingValues ->
        AppBackground(modifier = Modifier.fillMaxSize()) {
            when {
                isLoading -> Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.TopCenter
                ) {
                    CenterLoading()
                }
                hasError -> Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyState("⚠️", "Cocktail indisponible", "Vérifie ta connexion puis réessaie.")
                }
                cocktail == null -> Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyState("🍸", "Aucune recette", "Aucun cocktail n’a été trouvé.")
                }
                else -> CocktailDetailContent(
                    cocktail = cocktail!!,
                    isFavorite = isFavorite,
                    isRandom = drinkId == null,
                    onReload = { randomReload++ },
                    onFavoriteClick = {
                        cocktail?.let {
                            FavoritesManager.toggleFavorite(context, it.id, it.name)
                            isFavorite = FavoritesManager.isFavorite(context, it.id)
                            Toast.makeText(
                                context,
                                if (isFavorite) "Ajouté aux favoris" else "Retiré des favoris",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
private fun CocktailDetailContent(
    cocktail: Cocktail,
    isFavorite: Boolean,
    isRandom: Boolean,
    onReload: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(340.dp)
                .clip(RoundedCornerShape(34.dp))
        ) {
            AsyncImage(
                model = cocktail.image,
                contentDescription = cocktail.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.78f))
                        )
                    )
            )
            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .size(54.dp)
                    .background(Color.White.copy(alpha = 0.90f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) PrimarySmooth else TextSecondary
                )
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(22.dp)
            ) {
                Text(
                    text = if (isRandom) "Cocktail du moment" else "Recette complète",
                    style = MaterialTheme.typography.labelMedium,
                    color = AccentGold,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = cocktail.name,
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        if (isRandom) {
            Button(
                onClick = onReload,
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimarySmooth)
            ) {
                Text("Surprise me", fontWeight = FontWeight.ExtraBold)
            }
            Spacer(modifier = Modifier.height(18.dp))
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            val infoItems = listOf(cocktail.category, cocktail.alcoholic, cocktail.glass)
                .filter { !it.isNullOrBlank() }
                .map { it!! }
            infoItems.chunked(2).forEach { rowItems ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    rowItems.forEach { item ->
                        Box(modifier = Modifier.padding(end = 8.dp, bottom = 8.dp)) {
                            GradientPill(text = item)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        SectionTitle("Ingredients")
        PremiumCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(18.dp)) {
                val ingredients = cocktail.ingredientsList().filter { it.isNotBlank() }
                if (ingredients.isEmpty()) {
                    Text(
                        text = "No ingredients available",
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    ingredients.forEachIndexed { index, ingredient ->
                        IngredientLine(number = index + 1, text = ingredient.trim())
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        SectionTitle("Preparation")
        PremiumCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = cocktail.instructions ?: "No recipe available",
                modifier = Modifier.padding(18.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = TextDark,
                textAlign = TextAlign.Start
            )
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.ExtraBold,
        color = TextDark,
        modifier = Modifier.padding(bottom = 10.dp)
    )
}

@Composable
private fun IngredientLine(number: Int, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(34.dp),
            shape = CircleShape,
            color = SecondarySmooth.copy(alpha = 0.24f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = number.toString(),
                    style = MaterialTheme.typography.labelMedium,
                    color = PrimarySmooth,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = TextDark,
            modifier = Modifier.weight(1f)
        )
    }
}
