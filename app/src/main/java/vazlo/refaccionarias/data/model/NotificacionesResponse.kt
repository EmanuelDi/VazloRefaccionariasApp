package vazlo.refaccionarias.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificacionesResponse(
    @SerialName("estado") val estado: Int,
    @SerialName("mensaje") val mensaje: String,
    @SerialName("mensajes") val mensajes: MutableList<Mensaje>
)


@Serializable
data class Mensaje(
    @SerialName("id") val id: String,
    @SerialName("titulo") val titulo: String,
    @SerialName("mensaje") val mensaje: String,
    @SerialName("imagen") val imagen: String
)