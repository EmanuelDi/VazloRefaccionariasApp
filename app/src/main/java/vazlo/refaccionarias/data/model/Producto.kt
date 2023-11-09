package vazlo.refaccionarias.data.model

import androidx.compose.runtime.saveable.listSaver
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Producto(
    @SerialName("id")
    val id: String? = "",
    @SerialName("precio")
    val precio: String = "",
    @SerialName("foto")
    val foto: String? = "",
    @SerialName("descripcion")
    val descripcion: String? = "",
    @SerialName("linea_pos")
    val lineaPos: String? = "",
    @SerialName("nombreArticulo")
    val nombreArticulo: String? = "",
    @SerialName("disponible")
    val disponible: String = "",
    @SerialName("tabla")
    val tabla: List<RelacionadoDetalle>? = emptyList(),
    @SerialName("sucursales")
    val sucursales: List<Sucursal>? = emptyList(),
) {
    companion object {
        val Saver = listSaver<Producto, Any>(
            save = {
                listOf(
                    it.id!!,
                    it.precio!!,
                    it.foto!!,
                    it.descripcion!!,
                    it.lineaPos!!,
                    it.nombreArticulo!!,
                    it.disponible!!,
                    it.tabla!!,
                    it.sucursales!!,
                )
            },
            restore = {
                Producto(
                    it[0] as String,
                    it[1] as String,
                    it[2] as String,
                    it[3] as String,
                    it[4] as String,
                    it[5] as String,
                    it[6] as String,
                )
            }
        )
    }
}


