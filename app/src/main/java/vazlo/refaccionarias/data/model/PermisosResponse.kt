package vazlo.refaccionarias.data.model

import androidx.annotation.StringRes
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PermisosResponse(
    @SerialName("precios")
    val precios: Int? = 0,
    @SerialName("existencias")
    val existencias: Int? = 0,
    @SerialName("pedido")
    val pedido: Int? = 0,
    @SerialName("captura")
    val captura: Int? = 0,
    @SerialName("csv")
    val csv: Int? = 0,
    @SerialName("cotizacion")
    val cotizacion: Int? = 0,
    @SerialName("carrito")
    val carrito: Int? = 0,
    @SerialName("recompensas")
    val recompensas: Int? = 0,
    @SerialName("estado")
    val estado: Int? = 0,
    @SerialName("mensaje")
    val mensaje: String? = ""
)

