package vazlo.refaccionarias.data.model.catalagoElectronicoData

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Motor(
    @SerialName("cilindraje_id")
    val cilindraje_id: String,

    @SerialName("nombre_cilindraje")
    val nombre_cilindraje: String,

    @SerialName("nombre_litro")
    val nombre_litro: String,

    @SerialName("litro_id")
    val litro_id: String,
)