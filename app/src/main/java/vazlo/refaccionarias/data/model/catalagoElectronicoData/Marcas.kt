package vazlo.refaccionarias.data.model.catalagoElectronicoData

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Marcas(
    @SerialName("marca_id")
    val marca_id: String,
    @SerialName("nombre_marca")
    val nombre_marca: String
)