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



val mensajesPrueba = listOf(
    Mensaje("1", "Hola", "Este es un mensaje de prueba", "https://i.imgur.com/1.jpg"),
    Mensaje("2", "Adiós", "Este es otro mensaje de prueba", "https://i.imgur.com/2.jpg"),
    Mensaje("3", "Gracias", "Este es el último mensaje de prueba", "https://i.imgur.com/3.jpg")
)