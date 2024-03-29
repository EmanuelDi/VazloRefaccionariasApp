package vazlo.refaccionarias.data.model.users_y_permisosData

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
    @SerialName("permiso_entrar")
    val entrar: Int? = 0,
    @SerialName("estado_cuenta")
    val estadoCuenta: Int? = 0,
    @SerialName("permiso_factura")
    val permisoFactura: Int? = 0,
    @SerialName("permiso_complemento")
    val permisoComplemento: Int? = 0,
    @SerialName("permiso_nota")
    val permisoNota: Int? = 0,

    @SerialName("estado")
    val estado: Int? = 0,
    @SerialName("mensaje")
    val mensaje: String? = ""

)

