package vazlo.refaccionarias.data.model.eventosData

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




val listaMarcadoresPrueba = listOf(
    Marcador(
        urlFoto = "https://example.com/foto1.jpg",
        refacNombre = "Refaccionaria El Sol",
        telefono = "492 123 4567",
        lat = 23.1813,
        long = -102.8754,
        distancia = 5
    ),
    Marcador(
        urlFoto = "https://example.com/foto2.jpg",
        refacNombre = "Refaccionaria La Luna",
        telefono = "492 234 5678",
        lat = 23.1765,
        long = -102.8832,
        distancia = 7
    ),
    Marcador(
        urlFoto = "https://example.com/foto3.jpg",
        refacNombre = "Refaccionaria Las Estrellas",
        telefono = "492 345 6789",
        lat = 23.1698,
        long = -102.8910,
        distancia = 10
    )
)
