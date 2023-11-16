package vazlo.refaccionarias.ui.screens.estadisticas

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

data class CatalagoItem(
    val title: String,
    val destination: () -> Unit,
    @DrawableRes  val icon: Int,
    val color: Color,
    val permiso: Boolean,
    val colorTexto: Color,
    val colorIcono: Color
)

