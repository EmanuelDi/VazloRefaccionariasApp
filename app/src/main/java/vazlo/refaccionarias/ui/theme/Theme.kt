package vazlo.refaccionarias.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColors = darkColorScheme(
    primary = Azul_Vazlo,
    primaryContainer = Azul_Vazlo,
    secondaryContainer = Rojo_Vazlo,
    surface = Rojo_Vazlo,
    surfaceVariant = Gris_Vazlo,
    onSurface = Blanco,
    onSurfaceVariant = Negro,
    inverseSurface = Azul_Vazlo,
    secondary = Rojo_Vazlo,
    inverseOnSurface = Blanco,
    outline = Blanco,
    outlineVariant = Blanco,
    background = Blanco,
    error = Rojo_Vazlo,
    inversePrimary = Amarillo_Vazlo,
    onPrimary = Blanco,
)

private val LightColors = lightColorScheme(
    primary = Azul_Vazlo,
    primaryContainer = Azul_Vazlo,
    secondaryContainer = Rojo_Vazlo,
    surface = Rojo_Vazlo,
    surfaceVariant = Gris_Vazlo,
    onSurface = Blanco,
    onSurfaceVariant = Negro,
    inverseSurface = Azul_Vazlo,
    secondary = Rojo_Vazlo,
    inverseOnSurface = Blanco,
    outline = Blanco,
    outlineVariant = Blanco,
    background = Blanco,
    error = Rojo_Vazlo,
    inversePrimary = Amarillo_Vazlo,
    onPrimary = Blanco
)

@Composable
fun VazloRefaccionariasTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColors
        else -> LightColors
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}