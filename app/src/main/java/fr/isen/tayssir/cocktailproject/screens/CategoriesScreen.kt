package fr.isen.tayssir.cocktailproject.screens

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.isen.tayssir.cocktailproject.data.Category
import fr.isen.tayssir.cocktailproject.network.ApiClient
import fr.isen.tayssir.cocktailproject.ui.components.AppBackground
import fr.isen.tayssir.cocktailproject.ui.components.CenterLoading
import fr.isen.tayssir.cocktailproject.ui.components.EmptyState
import fr.isen.tayssir.cocktailproject.ui.components.PremiumCard
import fr.isen.tayssir.cocktailproject.ui.components.ScreenHeader
import fr.isen.tayssir.cocktailproject.ui.theme.PrimarySmooth
import fr.isen.tayssir.cocktailproject.ui.theme.SecondarySmooth
import fr.isen.tayssir.cocktailproject.ui.theme.SoftGreen
import fr.isen.tayssir.cocktailproject.ui.theme.SoftPurple
import fr.isen.tayssir.cocktailproject.ui.theme.SurfaceLight
import fr.isen.tayssir.cocktailproject.ui.theme.TextDark
import fr.isen.tayssir.cocktailproject.ui.theme.TextSecondary

@Composable
fun CategoriesScreen(
    onCategoryClick: (String) -> Unit
) {
    var categories by remember { mutableStateOf<List<Category>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoading = true
        hasError = false
        try {
            categories = ApiClient.apiService.getCategories().drinks ?: emptyList()
        } catch (e: Exception) {
            hasError = true
        }
        isLoading = false
    }

    AppBackground(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            ScreenHeader(
                title = "Categories",
                subtitle = "Explore les familles de cocktails et trouve ton style préféré."
            )

            Spacer(modifier = Modifier.height(18.dp))

            when {
                isLoading -> CenterLoading()
                hasError -> EmptyState("⚠️", "Impossible de charger", "Vérifie ta connexion puis relance l’application.")
                categories.isEmpty() -> EmptyState("🍸", "Aucune catégorie", "Les catégories apparaîtront ici.")
                else -> LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    itemsIndexed(categories) { index, category ->
                        CategoryItem(
                            category = category,
                            index = index,
                            onClick = { onCategoryClick(category.name) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryItem(category: Category, index: Int, onClick: () -> Unit) {
    val colors = listOf(PrimarySmooth, SecondarySmooth, SoftGreen, SoftPurple)
    val startColor = colors[index % colors.size]
    val endColor = colors[(index + 1) % colors.size]

    PremiumCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(58.dp),
                shape = CircleShape,
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(1.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(1.dp)
                    )
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        shape = CircleShape,
                        color = SurfaceLight
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(5.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(0.dp)
                                    .clickable(enabled = false) {}
                            )
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                shape = CircleShape,
                                color = startColor.copy(alpha = 0.16f)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(text = categoryEmoji(category.name), style = MaterialTheme.typography.headlineMedium)
                                }
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Voir les cocktails disponibles",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }

            Surface(
                modifier = Modifier.size(42.dp),
                shape = CircleShape,
                color = startColor.copy(alpha = 0.12f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                        tint = PrimarySmooth
                    )
                }
            }
        }
    }
}

private fun categoryEmoji(name: String): String {
    val value = name.lowercase()
    return when {
        "cocktail" in value -> "🍹"
        "shot" in value -> "🥃"
        "coffee" in value -> "☕"
        "beer" in value -> "🍺"
        "punch" in value -> "🍍"
        "shake" in value -> "🥤"
        "cocoa" in value -> "🍫"
        else -> "🍸"
    }
}
