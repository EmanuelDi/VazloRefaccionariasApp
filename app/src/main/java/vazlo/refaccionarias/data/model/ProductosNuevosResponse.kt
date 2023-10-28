package vazlo.refaccionarias.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import vazlo.refaccionarias.data.model.Producto

@Serializable
data class ProductosNuevosResponse(
    @SerialName("permiso_precio")
    val permisoPrecio: Int,
    @SerialName("permiso_existencia")
    val permisoExistencia: Int,
    @SerialName("permiso_cotizacion")
    val permisoCotizacion: Int,
    @SerialName("cedis")
    val cedis: String,
    @SerialName("clientes")
    val clientes: List<Producto>,
    @SerialName("estado")
    val estado: Int,
    @SerialName("mensaje")
    val mensaje: String
)
