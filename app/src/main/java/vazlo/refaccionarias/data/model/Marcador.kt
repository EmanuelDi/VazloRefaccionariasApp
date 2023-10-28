package vazlo.refaccionarias.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Marcador(
    @SerialName("url_foto")
    val urlFoto: String,
    @SerialName("refaccionaria_nombre")
    val refacNombre: String,
    @SerialName("telefono")
    val telefono: String,
    @SerialName("lat")
    val lat: Double,
    @SerialName("long")
    val long: Double,
    @SerialName("distancia")
    val distancia: Int
)
