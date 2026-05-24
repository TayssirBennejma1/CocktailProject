package fr.isen.tayssir.cocktailproject.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val SmoothColorScheme = lightColorScheme(
    primary = PrimarySmooth,
    secondary = SecondarySmooth,
    background = BackgroundLight,
    surface = SurfaceLight,
    onPrimary = SurfaceLight,
    onBackground = TextDark,
    onSurface = TextDark
)

@Composable
fun CocktailProjectTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = SmoothColorScheme,
        typography = Typography,
        content = content
    )
}