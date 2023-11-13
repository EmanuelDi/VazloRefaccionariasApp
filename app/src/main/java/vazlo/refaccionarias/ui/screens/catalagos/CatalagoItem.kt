package vazlo.refaccionarias.ui.screens.catalagos

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class CatalagoItem(
    val title: String,
    val destination: () -> Unit,
    @DrawableRes  val icon: Int,
    val color: Color,
    val permiso: Boolean,
    val colorTexto: Color,
    val colorIcono: Color
)

