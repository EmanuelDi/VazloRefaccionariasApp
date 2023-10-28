package vazlo.refaccionarias.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Producto(
    @SerialName("id")
    val id: String? = "",
    @SerialName("String")
    val precio: String? = "",
    @SerialName("foto")
    val foto: String? = "",
    @SerialName("descripcion")
    val descripcion: String,
    @SerialName("linea_pos")
    val lineaPos: String,
    @SerialName("nombreArticulo")
    val nombreArticulo: String,
    @SerialName("disponible")
    val disponible: String,
    @SerialName("tabla")
    val tabla: List<RelacionadoDetalle>
)


