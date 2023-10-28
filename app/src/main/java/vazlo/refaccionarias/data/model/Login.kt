package vazlo.refaccionarias.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Login(
    @SerialName("estado")
    val estado: Int,

    @SerialName("usuario")
    val usuario: String,

    @SerialName("nombre")
    val nombre: String,

    @SerialName("mensaje")
    val mensaje: String,

    @SerialName("responsable")
    val responsable: String? = "no",

    @SerialName("precio")
    val precio: String? = "no",

    @SerialName("existencia")
    val existencia: String? = "no",

    @SerialName("pedidos")
    val pedidos: String? = "no",

    @SerialName("captura")
    val captura: String? = "no",

    @SerialName("csv")
    val csv: String? = "no",

    @SerialName("cotizacion")
    val cotizacion: String? = "NO",

    @SerialName("otro_carrito")
    val otroCarrito: String? = "no",

    @SerialName("cedis")
    val cedis: String? ="no",

    )
