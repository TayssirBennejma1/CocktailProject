package fr.isen.tayssir.cocktailproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import fr.isen.tayssir.cocktailproject.screens.CategoriesScreen
import fr.isen.tayssir.cocktailproject.screens.DetailCocktailScreen
import fr.isen.tayssir.cocktailproject.ui.theme.CocktailProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CocktailProjectTheme {
                CategoriesScreen(
                    onCategoryClick = {}
                )
            }
        }
    }
}