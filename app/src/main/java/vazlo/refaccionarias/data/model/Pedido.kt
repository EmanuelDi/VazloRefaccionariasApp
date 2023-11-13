package vazlo.refaccionarias.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InfoPedido(
    @SerialName("folio")
    val folio: String,
    @SerialName("fecha_recepcion")
    val fecha_recepcion: String,
    @SerialName("fecha_atencion")
    val fecha_atencion: String,
    @SerialName("status")
    val status: String,
    @SerialName("comentarios")
    val comentarios: String,
    @SerialName("subtotal")
    val subtotal: String,
    @SerialName("iva")
    val iva: String,
    @SerialName("total")
    val total: String
)

@Serializable
data class ProductoPedido(
    @SerialName("url")
    val url: String,
    @SerialName("producto")
    val producto: String? = "",
    @SerialName("cantidad")
    val cantidad: String? = "",
    @SerialName("precio")
    val precio: String
)
