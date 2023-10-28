package vazlo.refaccionarias.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import vazlo.refaccionarias.data.model.ProductosResult

@Serializable
data class ResultadoPartes(
    @SerialName("permiso_precio")
    val permisoPrecio: Int? = 0,
    @SerialName("permiso_existencia")
    val permisoExistencia: Int? = 0,
    @SerialName("permiso_cotizacion")
    val permisoCotizacion: Int? = 0,
    @SerialName("lineas")
    val lineas: List<ProductosResult>,
    @SerialName("total")
    val total: Int? = 0,
    @SerialName("estado")
    val estado: Int,
    @SerialName("mensaje")
    val mensaje: String
)
