package vazlo.refaccionarias.data.model.busquedasData

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ResponseProductsCart(
    @SerialName("Info_Carrito")
    val productos: MutableList<ProductoCart>,
    @SerialName("permiso_precio")
    val permisoPrecio: Int,
    @SerialName("permiso_existencia")
    val permisoExistencia: Int,
    @SerialName("permiso_cotizacion")
    val permisoCotizacion: Int,
    @SerialName("permiso_pedido")
    val permisoPedido: Int,
    @SerialName("Subtotal")
    val subtotal: String? = "",
    @SerialName("IVA")
    val iva: String? = "",
    @SerialName("TOTAL")
    val total: String? = "",
    @SerialName("estado")
    val estado: Int? = 0,
    @SerialName("mensaje")
    val mensaje: String? = ""
)

@Serializable
data class ProductoCart(
    @SerialName("url")
    val url: String,
    @SerialName("nombre_soporte")
    val nombreSoporte: String,
    @SerialName("linea")
    val linea: String,
    @SerialName("cantidad")
    val cantidad: String,
    @SerialName("precio")
    val precio: String
)
